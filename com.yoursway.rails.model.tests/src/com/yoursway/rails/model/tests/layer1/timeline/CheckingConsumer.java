package com.yoursway.rails.model.tests.layer1.timeline;

import com.yoursway.model.repository.IConsumer;
import com.yoursway.model.repository.IResolver;

public abstract class CheckingConsumer implements IConsumer {

	private AssertionError error = null;
	private int callsCounter = 0;

	public void consume(IResolver resolver) {
		if (error == null) {
			try {
				consumeInternal(resolver);
			} catch (AssertionError e) {
				this.error = e;
			}
		}
		callsCounter++;
	}

	public int callsCount() {
		return callsCounter;
	}

	public void check() {
		if (error != null)
			throw error;
	}

	protected abstract void consumeInternal(IResolver resolver);

	public AssertionError error() {
	    return error;
	}
	
}
