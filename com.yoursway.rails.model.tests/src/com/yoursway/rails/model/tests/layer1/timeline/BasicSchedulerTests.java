package com.yoursway.rails.model.tests.layer1.timeline;

import java.text.DateFormat;
import java.text.ParseException;

import org.junit.Test;

import com.yoursway.model.repository.IResolver;
import com.yoursway.model.repository.Scheduler;

public class BasicSchedulerTests extends AbstractSchedulerTests {
    
    private final class ShitConsumer extends CheckingConsumer {
        
        public ShitConsumer(Class<?> modelRootClass) {
            shitHandle = new StubHandle("shit", modelRootClass);
            answerHandle = new StubHandle("answer", modelRootClass);
        }
        
        private StubHandle shitHandle;
        private StubHandle answerHandle;
        
        @Override
        protected void consumeInternal(IResolver resolver) {
            assertNotNull(resolver);
            if (callsCount() == 0) {
                assertNull(resolver.get(shitHandle));
                assertNull(resolver.get(answerHandle));
                assertNull(resolver.getIfAvail(shitHandle));
                assertNull(resolver.getIfAvail(answerHandle));
            } else {
                String str = resolver.get(shitHandle);
                assertNotNull(str);
                try {
                    assertNotNull(DateFormat.getInstance().parse(str));
                } catch (ParseException e) {
                    throw new AssertionError("date is not set!");
                }
                assertEquals(resolver.get(answerHandle), "42");
            }
        }
    }
    
    @Test
    public void nothing() {
        Scheduler scheduler = createScheduler();
        assertNotNull(scheduler);
        ShitConsumer consumer = new ShitConsumer(MockModelRoot.class);
        scheduler.addConsumer(consumer);
        consumer.check();
        assertEquals(1, consumer.callsCount());
    }
    
    @Test
    public void oneBasicModelOneConsumer() throws InterruptedException {
        Scheduler scheduler = createScheduler();
        assertNotNull(scheduler);
        ShitConsumer consumer = new ShitConsumer(MockModelRoot.class);
        scheduler.addConsumer(consumer);
        assertEquals(1, consumer.callsCount());
        // add model
        MockModelRoot mockModelRoot = new MockModelRoot();
        MockModel mockModel = new MockModel(scheduler, MockModelRoot.class, mockModelRoot);
        MockModelRoot root = scheduler.obtainRoot(MockModelRoot.class);
        assertEquals(root, mockModelRoot);
        for (int i = 0; i < 42; i++)
            mockModel.tick();
        assertEquals(43, consumer.callsCount());
        // check that a consumer is not called when the model is stopped
        consumer.check();
    }
    
    @Test
    public void oneBasicModelOneFailingConsumer() throws InterruptedException {
        Scheduler scheduler = createScheduler();
        assertNotNull(scheduler);
        ShitConsumer consumer = new ShitConsumer(MockModelRoot.class);
        scheduler.addConsumer(consumer);
        assertEquals(1, consumer.callsCount());
        // add model
        MockModelRoot mockModelRoot = new MockModelRoot();
        MockModel mockModel = new MockModel(scheduler, MockModelRoot.class, mockModelRoot);
        mockModel.setAnswer("43");
        mockModel.tick();
        assertEquals(2, consumer.callsCount());
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
        assertEquals(1, consumer1.callsCount());
        assertEquals(1, consumer2.callsCount());
        MockModel mockModel = new MockModel(scheduler, MockModelRoot.class, new MockModelRoot());
        for (int i = 0; i < 42; i++)
            mockModel.tick();
        assertEquals(43, consumer1.callsCount());
        assertEquals(43, consumer2.callsCount());
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
        assertEquals(1, consumer1.callsCount());
        assertEquals(1, consumer2.callsCount());
        MockModelRoot mockModelRoot = new MockModelRoot();
        MockModel mockModel = new MockModel(scheduler, MockModelRoot.class, mockModelRoot);
        AnotherMockModelRoot anotherMockModelRoot = new AnotherMockModelRoot();
        MockModel mockModel2 = new MockModel(scheduler, AnotherMockModelRoot.class, anotherMockModelRoot);
        assertEquals(mockModelRoot, scheduler.obtainRoot(MockModelRoot.class));
        assertEquals(anotherMockModelRoot, scheduler.obtainRoot(AnotherMockModelRoot.class));
        for (int i = 0; i < 42; i++)
            mockModel.tick();
        assertEquals(43, consumer1.callsCount());
        assertEquals(1, consumer2.callsCount());
        for (int i = 0; i < 42; i++)
            mockModel2.tick();
        assertEquals(43, consumer1.callsCount());
        assertEquals(43, consumer2.callsCount());
        consumer1.check();
        consumer2.check();
    }
    
}
