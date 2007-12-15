package com.yoursway.rails.model.tests.layer1.timeline;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Set;

import org.junit.Test;

import com.yoursway.model.repository.IConsumer;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelRoot;
import com.yoursway.model.repository.IResolver;
import com.yoursway.model.repository.Scheduler;
import com.yoursway.model.resource.internal.SnapshotBuilder;

public class ComplexSchedulerTests extends AbstractSchedulerTests {

	private interface IBridgeResolver {
		<V, H extends IHandle<V>> V get(IResolver resolver, H handle);
	}

	private static final int TIME_LIMIT = 100;

	private final static IModelRoot CALC_MODEL_ROOT_1 = new IModelRoot() {
	};
	private final static IModelRoot CALC_MODEL_ROOT_2 = new IModelRoot() {
	};

	private static final IBridgeResolver GET_RESOLVER = new IBridgeResolver() {

		public <V, H extends IHandle<V>> V get(IResolver resolver, H handle) {
			return resolver.get(handle);
		}
	};

	private static final IBridgeResolver GETIFAVAIL_RESOLVER = new IBridgeResolver() {

		public <V, H extends IHandle<V>> V get(IResolver resolver, H handle) {
			return resolver.getIfAvail(handle);
		}
	};

	private AbstractCalculatedMockModel cm;

	private void _test1(final IBridgeResolver proxy) throws Exception {
		Scheduler scheduler = createScheduler();
		assertNotNull(scheduler);
		final AbstractCalculatedMockModel model0 = new AbstractCalculatedMockModel(
				scheduler, CALC_MODEL_ROOT_1) {

			@Override
			public void updateInternal(IResolver resolver,
					SnapshotBuilder snapshotBuilder,
					Set<IHandle<?>> changedHandles) {
				snapshotBuilder.put(new StubHandle("answer", getKlass()), "42");
			}

		};
		final AssertionError error[] = new AssertionError[1];
		scheduler.addConsumer(new IConsumer() {

			public void consume(IResolver resolver) {
				try {
					assertNotNull(resolver);
					assertEquals("42", proxy.get(resolver, new StubHandle(
							"answer", model0.getModelRootInterface())));
					assertNull(proxy.get(resolver, new StubHandle("answer2",
							model0.getModelRootInterface())));
				} catch (AssertionError e) {
					error[0] = e;
				}
			}

		});
		if (error[0] != null)
			throw error[0];
		assertEquals(model0.updates(), 1);
	}

	private void waitForUpdates(AbstractCalculatedMockModel cm)
			throws InterruptedException {
		int i;
		for (i = 0; (i < TIME_LIMIT && cm.updates() == 0); i++)
			Thread.sleep(1);
		if (i == TIME_LIMIT)
			throw new AssertionError(
					"timeout, update() was not called, zaebals'a zhdat'");
	}

	private void waitForData(CheckingConsumer cc) throws InterruptedException {
		int init = cc.callsCount();
		int i;
		for (i = 0; (i < TIME_LIMIT && cc.callsCount() == init); i++)
			Thread.sleep(1);
		if (i == TIME_LIMIT)
			throw new AssertionError(
					"timeout, consume() was not called, zaebals'a zhdat'");
	}

	private AbstractCalculatedMockModel createNullCheckingCalculatedModel(
			Scheduler scheduler, final IBridgeResolver br,
			final boolean shouldBeNull) {
		return new AbstractCalculatedMockModel(scheduler, CALC_MODEL_ROOT_1) {

			@Override
			public void updateInternal(IResolver resolver,
					SnapshotBuilder snapshotBuilder,
					Set<IHandle<?>> changedHandles) {

				String string = br.get(resolver, new StubHandle("shit",
						MockModelRoot.class));
				if (shouldBeNull)
					assertNull(string);
				else
					assertNotNull(string);
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
		new MockModel(scheduler, MockModelRoot.class, new MockModelRoot());
		AbstractCalculatedMockModel CM = createNullCheckingCalculatedModel(
				scheduler, GET_RESOLVER, false);
		CM.check();
		assertEquals(1, CM.updates());
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

		cm = createNullCheckingCalculatedModel(scheduler, GET_RESOLVER, false);
		waitForUpdates(cm);
		new MockModel(scheduler, MockModelRoot.class, new MockModelRoot());
		cm.check();
		assertEquals(1, cm.updates());
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

		AbstractCalculatedMockModel CM = createNullCheckingCalculatedModel(
				scheduler, GETIFAVAIL_RESOLVER, true);
		waitForUpdates(CM);
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
		final StubHandle holyHandle = new StubHandle("holyshit",
				CALC_MODEL_ROOT_1.getClass());
		final StubHandle answerHandle = new StubHandle("answer",
				CALC_MODEL_ROOT_1.getClass());
		AbstractCalculatedMockModel CM = new AbstractCalculatedMockModel(
				scheduler, CALC_MODEL_ROOT_1) {

			@Override
			public void updateInternal(IResolver resolver,
					SnapshotBuilder snapshotBuilder,
					Set<IHandle<?>> changedHandles) {
				snapshotBuilder.put(answerHandle, "42");
				String string = resolver.get(new StubHandle("shit",
						MockModelRoot.class));
				snapshotBuilder.put(holyHandle, string);
			}

		};
		CM.check();
		waitForUpdates(CM);
		CheckingConsumer checkingConsumer = new CheckingConsumer() {

			@Override
			protected void consumeInternal(IResolver resolver) {
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
		checkingConsumer.check();
		// god loves trinity: just check whether we are updated about new
		// changes in the model
		waitForData(checkingConsumer);
		waitForData(checkingConsumer);
		waitForData(checkingConsumer);
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
		new MockModel(scheduler, MockModelRoot.class, new MockModelRoot());
		final StubHandle holyHandle = new StubHandle("holyshit",
				CALC_MODEL_ROOT_1.getClass());
		AbstractCalculatedMockModel CM = new AbstractCalculatedMockModel(
				scheduler, CALC_MODEL_ROOT_1) {

			@Override
			public void updateInternal(IResolver resolver,
					SnapshotBuilder snapshotBuilder,
					Set<IHandle<?>> changedHandles) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					throw new AssertionError("sleep failed");
				}
				String string = resolver.get(new StubHandle("shit",
						MockModelRoot.class));
				snapshotBuilder.put(holyHandle, string);
			}

		};
		CM.check();
		CheckingConsumer checkingConsumer = new CheckingConsumer() {

			@Override
			protected void consumeInternal(IResolver resolver) {
				if (!waitForResults) {
					if (callsCount() == 1) {
						String date = resolver.getIfAvail(holyHandle);
						assertNull(date);
					} else {
						String date = resolver.getIfAvail(holyHandle);
						assertNotNull(date);
					}
				} else {
					String date = resolver.get(holyHandle);
					assertNotNull(date);
				}

			}

		};
		scheduler.addConsumer(checkingConsumer);
		checkingConsumer.check();
		waitForData(checkingConsumer);
		checkingConsumer.check();
	}

	@Test
	public void test3_wait() throws Exception {
		_test3(true);

	}

	@Test
	public void test3_block() throws Exception {
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
		new MockModel(scheduler, MockModelRoot.class, new MockModelRoot());
		final StubHandle milliHandle = new StubHandle("milli",
				MockModelRoot.class);
		final StubHandle milli2Handle = new StubHandle("milli2",
				CALC_MODEL_ROOT_1.getClass());
		AbstractCalculatedMockModel CM1 = new AbstractCalculatedMockModel(
				scheduler, CALC_MODEL_ROOT_1) {

			@Override
			public void updateInternal(IResolver resolver,
					SnapshotBuilder snapshotBuilder,
					Set<IHandle<?>> changedHandles) {

				String m = resolver.get(milliHandle);
				assertNotNull(m);

				String oldValue = resolver.get(milli2Handle);
				if (this.updates() > 1)
					assertNotNull(oldValue);
				snapshotBuilder.put(milli2Handle, m);
			}

		};
		final StubHandle milli3Handle = new StubHandle("milli3",
				CALC_MODEL_ROOT_2.getClass());
		AbstractCalculatedMockModel CM2 = new AbstractCalculatedMockModel(
				scheduler, CALC_MODEL_ROOT_2) {

			@Override
			public void updateInternal(IResolver resolver,
					SnapshotBuilder snapshotBuilder,
					Set<IHandle<?>> changedHandles) {

				String m = resolver.get(new StubHandle("milli",
						MockModelRoot.class));
				assertNotNull(m);
				String milli2 = resolver.get(milli2Handle);
				assertNotNull(milli2);

				// XXX: is it correct?
				assertEquals(milli2, m);

				snapshotBuilder.put(milli3Handle, m + " " + milli2);
			}

		};
		CheckingConsumer checkingConsumer = new CheckingConsumer() {

			@Override
			protected void consumeInternal(IResolver resolver) {
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
		Thread.sleep(1000);
		waitForUpdates(CM1);
		waitForUpdates(CM2);
		waitForData(checkingConsumer);
		CM1.check();
		CM2.check();
		checkingConsumer.check();
	}

}
