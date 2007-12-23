package com.yoursway.rails.model.tests.layer1.timeline;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Set;

import org.junit.Test;

import com.yoursway.model.repository.IConsumer;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelRoot;
import com.yoursway.model.repository.IResolver;
import com.yoursway.model.repository.NoSuchHandleException;
import com.yoursway.model.repository.Scheduler;
import com.yoursway.model.resource.internal.SnapshotBuilder;

public class ComplexSchedulerTests extends AbstractSchedulerTests {
    
    private interface IBridgeResolver {
        <V, H extends IHandle<V>> V get(IResolver resolver, H handle) throws NoSuchHandleException;
    }
    
    private final static IModelRoot CALC_MODEL_ROOT_1 = new IModelRoot() {
    };
    private final static IModelRoot CALC_MODEL_ROOT_2 = new IModelRoot() {
    };
    
    private static final IBridgeResolver GET_RESOLVER = new IBridgeResolver() {
        
        public <V, H extends IHandle<V>> V get(IResolver resolver, H handle) throws NoSuchHandleException {
            return resolver.get(handle);
        }
    };
    
    private static final IBridgeResolver GETIFAVAIL_RESOLVER = new IBridgeResolver() {
        
        public <V, H extends IHandle<V>> V get(IResolver resolver, H handle) throws NoSuchHandleException {
            return resolver.getIfAvail(handle);
        }
    };
    
    private AbstractCalculatedMockModel cm;
    
    private void _test1(final IBridgeResolver proxy) throws Exception {
        Scheduler scheduler = createScheduler();
        assertNotNull(scheduler);
        final AbstractCalculatedMockModel model0 = new AbstractCalculatedMockModel(scheduler,
                CALC_MODEL_ROOT_1) {
            
            @Override
            public void updateInternal(IResolver resolver, SnapshotBuilder snapshotBuilder,
                    Set<IHandle<?>> changedHandles) {
                System.out.println(".updateInternal()");
                snapshotBuilder.put(new StubHandle("answer", getKlass()), "42");
            }
            
        };
        
        waitForUpdates(model0, 1);
        final AssertionError error[] = new AssertionError[1];
        scheduler.addConsumer(new IConsumer() {
            
            public void consume(IResolver resolver) {
                try {
                    assertNotNull(resolver);
                    try {
                        assertEquals("42", proxy.get(resolver, new StubHandle("answer", model0
                                .getModelRootInterface())));
                    } catch (NoSuchHandleException e) {
                        throw new AssertionError(e);
                    }
                } catch (AssertionError e) {
                    error[0] = e;
                }
            }
            
        });
        assertEquals(model0.updates(), 1);
        if (error[0] != null)
            throw error[0];
    }
    
    private void waitForUpdates(AbstractCalculatedMockModel cm, int count) throws InterruptedException {
        while (cm.updates() < count)
            Thread.sleep(10);
    }
    
    private AbstractCalculatedMockModel createNullCheckingCalculatedModel(Scheduler scheduler,
            final IBridgeResolver br, final boolean shouldBeNull) {
        return new AbstractCalculatedMockModel(scheduler, CALC_MODEL_ROOT_1) {
            
            @Override
            public void updateInternal(IResolver resolver, SnapshotBuilder snapshotBuilder,
                    Set<IHandle<?>> changedHandles) throws NoSuchHandleException {
                System.out.println(".updateInternal()");
                if (shouldBeNull) {
                    try {
                        String string = br.get(resolver, new StubHandle("shit", MockModelRoot.class));
                        assertNull(string);
                    } catch (NoSuchHandleException e) {
                        //ok
                    }
                } else {
                    String string = br.get(resolver, new StubHandle("shit", MockModelRoot.class));
                    assertNotNull(string);
                }
            }
            
        };
    }
    
    /**
     * test1: one calc.model, no basic models. model should be updated once
     * model should post handles handles should be accessible for consumers,
     * consume() should be called only once model should not be called for
     * update more than one time
     */
    @Test
    public void test1() throws Exception {
        _test1(GET_RESOLVER);
        _test1(GETIFAVAIL_RESOLVER);
    }
    
    /*
     * test2: one basic model, one dependent calc.model basic model is timer
     * (see BasicSchedulerTests) calc. model doest smt with "time" and "answer"
     * (concatenates and computes hash for ex.) note 0: there should be a switch
     * for making calc.model frozen note 1: calc. model should update quickly
     * check TWO cases: basic model is created first and calc.model is created
     * first 1) CM should be updated with a fresh BM values 2) CM should not be
     * updated before consumer called now we add consumer 3) CM should be
     * updated and consumer should be called too freeze CM 4) CM should not be
     * updated more and consumer should not be called too
     */
    @Test
    public void test2_order1() throws Exception {
        Scheduler scheduler = createScheduler();
        assertNotNull(scheduler);
        MockModel mockModel = new MockModel(scheduler, MockModelRoot.class, new MockModelRoot());
        mockModel.tick();
        AbstractCalculatedMockModel CM = createNullCheckingCalculatedModel(scheduler, GET_RESOLVER, false);
        waitForUpdates(CM, 1);
        CM.check();
    }
    
    /**
     * inversed order: CM, then BM
     * 
     * @throws Exception
     */
    @Test
    public void test2_order2() throws Exception {
        Scheduler scheduler = createScheduler();
        assertNotNull(scheduler);
        cm = createNullCheckingCalculatedModel(scheduler, GET_RESOLVER, true);
        waitForUpdates(cm, 1);
        MockModel mockModel = new MockModel(scheduler, MockModelRoot.class, new MockModelRoot());
        mockModel.tick();
        waitForUpdates(cm, 2);
        assertNotNull(cm.getError());
    }
    
    /**
     * inversed order: CM, then BM, but getIfAvail() is called
     * 
     * @throws Exception
     */
    @Test
    public void test2_order3() throws Exception {
        Scheduler scheduler = createScheduler();
        assertNotNull(scheduler);
        
        AbstractCalculatedMockModel CM = createNullCheckingCalculatedModel(scheduler, GETIFAVAIL_RESOLVER,
                true);
        waitForUpdates(CM, 1);
        new MockModel(scheduler, MockModelRoot.class, new MockModelRoot());
        CM.check();
        assertEquals(1, CM.updates());
    }
    
    /**
     * Now we add a consumer...
     * 
     * @throws Exception
     */
    @Test
    public void test2_consumer_cums() throws Exception {
        Scheduler scheduler = createScheduler();
        assertNotNull(scheduler);
        new MockModel(scheduler, MockModelRoot.class, new MockModelRoot());
        final StubHandle holyHandle = new StubHandle("holyshit", CALC_MODEL_ROOT_1.getClass());
        final StubHandle answerHandle = new StubHandle("answer", CALC_MODEL_ROOT_1.getClass());
        AbstractCalculatedMockModel CM = new AbstractCalculatedMockModel(scheduler, CALC_MODEL_ROOT_1) {
            
            @Override
            public void updateInternal(IResolver resolver, SnapshotBuilder snapshotBuilder,
                    Set<IHandle<?>> changedHandles) throws NoSuchHandleException {
                snapshotBuilder.put(answerHandle, "42");
                String string = resolver.get(new StubHandle("shit", MockModelRoot.class));
                snapshotBuilder.put(holyHandle, string);
            }
            
        };
        CM.check();
        waitForUpdates(CM, 1);
        CheckingConsumer checkingConsumer = new CheckingConsumer() {
            
            @Override
            protected void consumeInternal(IResolver resolver) throws NoSuchHandleException {
                String date = resolver.get(holyHandle);
                assertNotNull(date);
                try {
                    assertNotNull(DateFormat.getInstance().parse(date));
                } catch (ParseException e) {
                    throw new AssertionError("date is not set!");
                }
                String ans = resolver.get(answerHandle);
                assertEquals("42", ans);
            }
            
        };
        scheduler.addConsumer(checkingConsumer);
        while (checkingConsumer.callsCount() < 1)
            Thread.sleep(10);
        checkingConsumer.check();
    }
    
    /*
     * test3: one BM, one CM BM is a timer CM is performs some complex task with
     * BM's values (>1 sec) 1) CM is updated with a fresh values 2) consumer
     * should fetch null as result for getOrNull() 3) after 1 sec consumer
     * should get result
     */

    public void _test3(final boolean waitForResults) throws Exception {
        Scheduler scheduler = createScheduler();
        assertNotNull(scheduler);
        MockModel mockModel = new MockModel(scheduler, MockModelRoot.class, new MockModelRoot());
        mockModel.tick();
        final StubHandle holyHandle = new StubHandle("holyshit", CALC_MODEL_ROOT_1.getClass());
        AbstractCalculatedMockModel CM = new AbstractCalculatedMockModel(scheduler, CALC_MODEL_ROOT_1) {
            
            @Override
            public void updateInternal(IResolver resolver, SnapshotBuilder snapshotBuilder,
                    Set<IHandle<?>> changedHandles) throws NoSuchHandleException {
                try {
                    Thread.sleep(1000); // simulate a long work
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new AssertionError("sleep failed");
                }
                System.out.println(".updateInternal()");
                String string = resolver.get(new StubHandle("shit", MockModelRoot.class));
                snapshotBuilder.put(holyHandle, string);
            }
            
        };
        waitForUpdates(CM, 1);
        CM.check();
        CheckingConsumer checkingConsumer = new CheckingConsumer() {
            
            @Override
            protected void consumeInternal(IResolver resolver) throws NoSuchHandleException {
                System.out.println(".consumeInternal()");
                if (waitForResults) {
                    String date = resolver.get(holyHandle);
                    assertNotNull(date);
                } else {
                    if (callsCount() == 1) {
                        String date = resolver.getIfAvail(holyHandle);
                        assertNull(date);
                    } else {
                        String date = resolver.getIfAvail(holyHandle);
                        assertNotNull(date);
                    }
                }
                
            }
            
        };
        scheduler.addConsumer(checkingConsumer);
        while (checkingConsumer.callsCount() < 1)
            Thread.sleep(10);
        checkingConsumer.check();
        mockModel.tick();
        while (checkingConsumer.callsCount() < 1)
            Thread.sleep(10);
        checkingConsumer.check();
    }
    
    @Test
    public void test3_wait() throws Exception {
        _test3(true);
        
    }
    
    @Test
    public void test3_null() throws Exception {
        _test3(false);
        
    }
    
    /*
     * test4: BM -- user-hitable model (updates only when we ask) dep. graph is
     * following: CM0 -> BM CM1 -> BM, CM0
     * 
     * CMx should be able to update quickly
     * 
     * 1) CMx should be created and initally updated 2) after hitting the BM,
     * CMx should be updated accordingly, consumers should be called 3) changes
     * in BM.handles that are unrelevant to CM1 and are relevant for CM0 should
     * not be sent to CM1, but should be sent to CM0
     * 
     */

    @Test
    public void test4() throws Exception {
        Scheduler scheduler = createScheduler();
        MockModel mockModel = new MockModel(scheduler, MockModelRoot.class, new MockModelRoot());
        mockModel.tick();
        final StubHandle milliHandle = new StubHandle("milli", MockModelRoot.class);
        final StubHandle milli2Handle = new StubHandle("milli2", CALC_MODEL_ROOT_1.getClass());
        AbstractCalculatedMockModel CM1 = new AbstractCalculatedMockModel(scheduler, CALC_MODEL_ROOT_1) {
            
            @Override
            public void updateInternal(IResolver resolver, SnapshotBuilder snapshotBuilder,
                    Set<IHandle<?>> changedHandles) throws NoSuchHandleException {
                
                String m = resolver.get(milliHandle);
                assertNotNull(m);
                
                String oldValue = resolver.get(milli2Handle);
                if (this.updates() > 1)
                    assertNotNull(oldValue);
                snapshotBuilder.put(milli2Handle, m);
            }
            
        };
        final StubHandle milli3Handle = new StubHandle("milli3", CALC_MODEL_ROOT_2.getClass());
        AbstractCalculatedMockModel CM2 = new AbstractCalculatedMockModel(scheduler, CALC_MODEL_ROOT_2) {
            
            @Override
            public void updateInternal(IResolver resolver, SnapshotBuilder snapshotBuilder,
                    Set<IHandle<?>> changedHandles) throws NoSuchHandleException {
                
                String m = resolver.get(new StubHandle("milli", MockModelRoot.class));
                assertNotNull(m);
                String milli2 = resolver.get(milli2Handle);
                assertNotNull(milli2);
                
//                // XXX: is it correct?
//                assertEquals(milli2, m);
//                
                snapshotBuilder.put(milli3Handle, m + " " + milli2);
            }
            
        };
        mockModel.tick();
        // wait until everything will be updated
        waitForUpdates(CM1, 2);
        waitForUpdates(CM2, 2);
        CheckingConsumer checkingConsumer = new CheckingConsumer() {
            
            @Override
            protected void consumeInternal(IResolver resolver) throws NoSuchHandleException {
                String milli = resolver.get(milliHandle);
                assertNotNull(milli);
                String milli2 = resolver.get(milli2Handle);
                assertNotNull(milli2);
                String milli3 = resolver.get(milli3Handle);
                assertNotNull(milli3);
                // TODO: add more complex checks for consistency
            }
            
        };
        scheduler.addConsumer(checkingConsumer);
        while (checkingConsumer.callsCount() < 1)
            Thread.sleep(10);
        CM1.check();
        CM2.check();
        checkingConsumer.check();
    }
    
}
