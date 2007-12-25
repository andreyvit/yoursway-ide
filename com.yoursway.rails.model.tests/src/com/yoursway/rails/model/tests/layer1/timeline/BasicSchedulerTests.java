package com.yoursway.rails.model.tests.layer1.timeline;

import org.junit.Test;

import com.yoursway.model.repository.IModelRoot;
import com.yoursway.model.repository.IResolver;
import com.yoursway.model.repository.NoSuchHandleException;
import com.yoursway.model.repository.Scheduler;

public class BasicSchedulerTests extends AbstractSchedulerTests {
    
    private final class ShitConsumer extends CheckingConsumer {
        
        public ShitConsumer(Class<? extends IModelRoot> modelRootClass) {
            shitHandle = new StubHandle("shit", modelRootClass);
            answerHandle = new StubHandle("answer", modelRootClass);
        }
        
        private StubHandle shitHandle;
        private StubHandle answerHandle;
        
        @Override
        protected void consumeInternal(IResolver resolver) throws NoSuchHandleException {
            assertNotNull(resolver);
            if (callsCount() == 0) {
                assertNull(resolver.get(shitHandle));
                assertNull(resolver.get(answerHandle));
                assertNull(resolver.getIfAvail(shitHandle));
                assertNull(resolver.getIfAvail(answerHandle));
            } else {
                String str = resolver.get(shitHandle);
                System.out.println(str);
                assertNotNull(str);                
                assertEquals(resolver.get(answerHandle), "42");
            }
        }
    }
    
    @Test
    public void nothing() throws Exception {
        Scheduler scheduler = createScheduler();
        assertNotNull(scheduler);
        ShitConsumer consumer = new ShitConsumer(MockModelRoot.class);
        scheduler.addConsumer(consumer);
        consumer.check();
        while (consumer.callsCount() < 1)
            Thread.sleep(10);
    }
    
    @Test
    public void oneBasicModelOneConsumer() throws InterruptedException {
        Scheduler scheduler = createScheduler();
        assertNotNull(scheduler);
        ShitConsumer consumer = new ShitConsumer(MockModelRoot.class);
        scheduler.addConsumer(consumer);
        while (consumer.callsCount() < 1)
            Thread.sleep(10);
        // add model
        MockModelRoot mockModelRoot = new MockModelRoot();
        MockModel mockModel = new MockModel(scheduler, MockModelRoot.class, mockModelRoot);
        MockModelRoot root = scheduler.obtainRoot(MockModelRoot.class);
        assertEquals(root, mockModelRoot);
        for (int i = 0; i < 42; i++)
            mockModel.tick();
        while (consumer.callsCount() < 43)
            Thread.sleep(10);
        // check that a consumer is not called when the model is stopped
        consumer.check();
    }
    
    @Test
    public void oneBasicModelOneFailingConsumer() throws InterruptedException {
        Scheduler scheduler = createScheduler();
        assertNotNull(scheduler);
        ShitConsumer consumer = new ShitConsumer(MockModelRoot.class);
        scheduler.addConsumer(consumer);
        while (consumer.callsCount() < 1)
            Thread.sleep(10);
        // add model
        MockModelRoot mockModelRoot = new MockModelRoot();
        MockModel mockModel = new MockModel(scheduler, MockModelRoot.class, mockModelRoot);
        mockModel.setAnswer("43");
        mockModel.tick();
        while (consumer.callsCount() < 2)
            Thread.sleep(10);
        assertNotNull(consumer.error()); // bad value, should fail
    }
    
    @Test
    public void oneBasicModelSeveralConsumers() throws InterruptedException {
        Scheduler scheduler = createScheduler();
        assertNotNull(scheduler);
        ShitConsumer consumer1 = new ShitConsumer(MockModelRoot.class);
        ShitConsumer consumer2 = new ShitConsumer(MockModelRoot.class);
        scheduler.addConsumer(consumer1);
        scheduler.addConsumer(consumer2);
        Thread.sleep(100);
        while (consumer1.callsCount() < 1)
            Thread.sleep(10);
        while (consumer2.callsCount() < 1)
            Thread.sleep(10);
        MockModel mockModel = new MockModel(scheduler, MockModelRoot.class, new MockModelRoot());
        for (int i = 0; i < 42; i++)
            mockModel.tick();
        while (consumer1.callsCount() < 43)
            Thread.sleep(10);
        while (consumer2.callsCount() < 43)
            Thread.sleep(10);
        consumer1.check();
        consumer2.check();
    }
    
    @Test
    public void twoBasicModelsSeveralConsumers() throws InterruptedException {
        Scheduler scheduler = createScheduler();
        assertNotNull(scheduler);
        ShitConsumer consumer1 = new ShitConsumer(MockModelRoot.class);
        ShitConsumer consumer2 = new ShitConsumer(AnotherMockModelRoot.class);
        scheduler.addConsumer(consumer1);
        scheduler.addConsumer(consumer2);
        while (consumer1.callsCount() < 1)
            Thread.sleep(10);
        while (consumer2.callsCount() < 1)
            Thread.sleep(10);
        MockModelRoot mockModelRoot = new MockModelRoot();
        MockModel mockModel = new MockModel(scheduler, MockModelRoot.class, mockModelRoot);
        AnotherMockModelRoot anotherMockModelRoot = new AnotherMockModelRoot();
        MockModel mockModel2 = new MockModel(scheduler, AnotherMockModelRoot.class, anotherMockModelRoot);
        assertEquals(mockModelRoot, scheduler.obtainRoot(MockModelRoot.class));
        assertEquals(anotherMockModelRoot, scheduler.obtainRoot(AnotherMockModelRoot.class));
        for (int i = 0; i < 42; i++)
            mockModel.tick();
        while (consumer1.callsCount() < 43)
            Thread.sleep(10);
        assertEquals(1, consumer2.callsCount());
        for (int i = 0; i < 42; i++)
            mockModel2.tick();
        while (consumer2.callsCount() < 43)
            Thread.sleep(10);
        assertEquals(43, consumer1.callsCount());
        consumer1.check();
        consumer2.check();
    }
    
}
