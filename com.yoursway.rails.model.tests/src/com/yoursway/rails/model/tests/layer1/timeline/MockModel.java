/**
 * 
 */
package com.yoursway.rails.model.tests.layer1.timeline;

import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import com.yoursway.model.repository.BasicModelDelta;
import com.yoursway.model.repository.IBasicModelChangesRequestor;
import com.yoursway.model.repository.IBasicModelRegistry;
import com.yoursway.model.repository.IModelRoot;
import com.yoursway.model.resource.internal.SnapshotBuilder;

class MockModel {
	private final IBasicModelChangesRequestor requestor;
	private Timer timer;
	int ticks;
	private final Class<?> rootClass;
	private String answer;

	@SuppressWarnings("unchecked")
	public MockModel(IBasicModelRegistry registry, Class<?> rootClass,
			IModelRoot root) {
		this.rootClass = rootClass;
		requestor = registry.addBasicModel(((Class<IModelRoot>) rootClass),
				root);
		timer = new Timer();
		ticks = 0;
		answer = "42";
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				tick();
			}
		}, 0, 50);
	}

	public void stop() {
		timer.cancel();
	}

	public int ticks() {
		return ticks;
	}

	public void setAnswer(String ans) {
		this.answer = ans;
	}

	private void tick() {
		ticks++;
		SnapshotBuilder sb = new SnapshotBuilder();
		sb.put(new StubHandle("shit", rootClass), GregorianCalendar
				.getInstance().getTime().toString());
		sb.put(new StubHandle("milli", rootClass), Long.toString(System.currentTimeMillis()));
		sb.put(new StubHandle("answer", rootClass), answer);
		requestor.theGivenPieceOfShitChanged(sb.getSnapshot(),
				new BasicModelDelta(sb.getChangedHandles(), sb
						.getAddedElements(), sb.getRemovedElements()));

	}

}