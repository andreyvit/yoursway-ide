/**
 * 
 */
package com.yoursway.rails.model.tests.layer1.timeline;

import com.yoursway.model.repository.IHandle;

class StubHandle implements IHandle<String> {
	/**
	 * 
	 */
	private final String magicString;
	private final Class<?> modelRootClass;

	public StubHandle(String magicString, Class<?> mrc) {
		this.magicString = magicString;
		this.modelRootClass = mrc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((magicString == null) ? 0 : magicString.hashCode());
		result = prime
				* result
				+ ((modelRootClass == null) ? 0 : modelRootClass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof StubHandle))
			return false;
		final StubHandle other = (StubHandle) obj;
		if (magicString == null) {
			if (other.magicString != null)
				return false;
		} else if (!magicString.equals(other.magicString))
			return false;
		if (modelRootClass == null) {
			if (other.modelRootClass != null)
				return false;
		} else if (!modelRootClass.equals(other.modelRootClass))
			return false;
		return true;
	}

	public Class<?> getModelRootInterface() {
		return modelRootClass;
	}

}