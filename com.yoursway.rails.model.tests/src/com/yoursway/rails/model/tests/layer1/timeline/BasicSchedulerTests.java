package com.yoursway.rails.model.tests.layer1.timeline;

import java.text.DateFormat;
import java.text.ParseException;

import org.junit.Test;

import com.yoursway.model.repository.IConsumer;
import com.yoursway.model.repository.IResolver;
import com.yoursway.model.repository.Scheduler;

public class BasicSchedulerTests extends AbstractSchedulerTests {

	private final class ShitConsumer implements IConsumer {

		public ShitConsumer(Class<?> modelRootClass) {
			shitHandle = new StubHandle("shit", modelRootClass);
			answerHandle = new StubHandle("answer", modelRootClass);
		}

		private int ticks = 0;
		private AssertionError error = null;
		private StubHandle shitHandle;
		private StubHandle answerHandle;

		public void consume(IResolver resolver) {
			if (error != null)
				return;
			try {
				work(resolver);
			} catch (AssertionError e) {
				error = e;
				e.printStackTrace();
			}
		}

		private void work(IResolver resolver) {
			assertNotNull(resolver);
			if (ticks == 0) {
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
			ticks++;
		}

		public int ticks() {
			return ticks;
		}
	}

	@Test
	public void nothing() {
		Scheduler scheduler = createScheduler();
		assertNotNull(scheduler);
		final AssertionError[] t = new AssertionError[1];
		scheduler.addConsumer(new IConsumer() {

			private boolean dead = false;

			public void consume(IResolver resolver) {
				try {
					assertNotNull(resolver);
					assertFalse(dead); // check that consume() was called only
					// once
					dead = true;
					String str = resolver.get(new StubHandle("shit",
							MockModelRoot.class));
					assertNull(str);
					String str2 = resolver.get(new StubHandle("answer",
							MockModelRoot.class));
					assertNull(str2);
					str = resolver.getIfAvail(new StubHandle("shit",
							MockModelRoot.class));
					assertNull(str);
					str2 = resolver.getIfAvail(new StubHandle("answer",
							MockModelRoot.class));
					assertNull(str2);
				} catch (AssertionError e) {
					t[0] = e;
				}
			}

		});
		if (t[0] != null)
			throw t[0];
	}

	@Test
	public void oneBasicModelOneConsumer() throws InterruptedException {
		Scheduler scheduler = createScheduler();
		assertNotNull(scheduler);
		ShitConsumer consumer = new ShitConsumer(MockModelRoot.class);
		scheduler.addConsumer(consumer);
		// check that a consumer is not called more than once without need
		Thread.sleep(100);
		assertEquals(1, consumer.ticks);
		// add model
		MockModelRoot mockModelRoot = new MockModelRoot();
		MockModel mockModel = new MockModel(scheduler, MockModelRoot.class,
				mockModelRoot);
		MockModelRoot root = scheduler.obtainRoot(MockModelRoot.class);
		assertEquals(root, mockModelRoot);
		// check that consumer is being updated
		while (mockModel.ticks < 42)
			Thread.sleep(10);
		assertTrue(consumer.ticks >= 42);
		// check that a consumer is not called when the model is stopped
		mockModel.stop();
		int old = consumer.ticks;
		while (mockModel.ticks < 42)
			Thread.sleep(10);
		assertEquals(old, consumer.ticks);
		if (consumer.error != null)
			throw consumer.error;
	}

	@Test
	public void oneBasicModelOneFailingConsumer() throws InterruptedException {
		Scheduler scheduler = createScheduler();
		assertNotNull(scheduler);
		ShitConsumer consumer = new ShitConsumer(MockModelRoot.class);
		scheduler.addConsumer(consumer);
		// check that a consumer is not called more than once without need
		Thread.sleep(100);
		assertEquals(consumer.ticks, 1);
		// add model
		MockModelRoot mockModelRoot = new MockModelRoot();
		MockModel mockModel = new MockModel(scheduler, MockModelRoot.class,
				mockModelRoot);
		mockModel.setAnswer("43");
		while (consumer.error == null && consumer.ticks == 1)
			Thread.sleep(100);
		assertNotNull(consumer.error); // bad value, should fail
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
		assertEquals(consumer1.ticks, 1);
		assertEquals(consumer2.ticks, 1);
		MockModel mockModel = new MockModel(scheduler, MockModelRoot.class,
				new MockModelRoot());
		// check that both consumers are being updated
		while (mockModel.ticks < 42)
			Thread.sleep(10);
		assertTrue(consumer1.ticks >= 42);
		assertTrue(consumer2.ticks >= 42);
		// check that consumers are not called when the model is stopped
		mockModel.stop();
		int old1 = consumer1.ticks;
		int old2 = consumer2.ticks;
		while (mockModel.ticks < 42)
			Thread.sleep(10);
		assertEquals(old1, consumer1.ticks);
		assertEquals(old2, consumer2.ticks);

		if (consumer1.error != null)
			throw consumer1.error;
		if (consumer2.error != null)
			throw consumer2.error;
	}

	@Test
	public void twoBasicModelsSeveralConsumers() throws InterruptedException {
		Scheduler scheduler = createScheduler();
		assertNotNull(scheduler);
		ShitConsumer consumer1 = new ShitConsumer(MockModelRoot.class);
		ShitConsumer consumer2 = new ShitConsumer(AnotherMockModelRoot.class);
		scheduler.addConsumer(consumer1);
		scheduler.addConsumer(consumer2);
		Thread.sleep(100);
		assertEquals(consumer1.ticks, 1);
		assertEquals(consumer2.ticks, 1);
		MockModelRoot mockModelRoot = new MockModelRoot();
		MockModel mockModel = new MockModel(scheduler, MockModelRoot.class,
				mockModelRoot);
		AnotherMockModelRoot anotherMockModelRoot = new AnotherMockModelRoot();
		MockModel mockModel2 = new MockModel(scheduler,
				AnotherMockModelRoot.class, anotherMockModelRoot);
		assertEquals(mockModelRoot, scheduler.obtainRoot(MockModelRoot.class));
		assertEquals(anotherMockModelRoot, scheduler
				.obtainRoot(AnotherMockModelRoot.class));
		// check that both consumers are being updated
		while (mockModel.ticks < 42)
			Thread.sleep(10);
		assertTrue(consumer1.ticks >= 42);
		assertTrue(consumer2.ticks >= 42);
		// check that first customer is not called when the model1 is stopped
		// and customer2 continues to work
		mockModel.stop();
		int old1 = consumer1.ticks;
		int old2 = consumer2.ticks;
		while (mockModel.ticks < 42)
			Thread.sleep(10);
		assertEquals(old1, consumer1.ticks);
		assertTrue(old2 < consumer2.ticks + 42);
		// check that after stopping both models nothing is updated
		mockModel2.stop();
		old2 = consumer2.ticks;
		while (mockModel.ticks < 42)
			Thread.sleep(10);
		assertEquals(old2, consumer2.ticks);
		// last check
		if (consumer1.error != null)
			throw consumer1.error;
		if (consumer2.error != null)
			throw consumer2.error;
	}

}
