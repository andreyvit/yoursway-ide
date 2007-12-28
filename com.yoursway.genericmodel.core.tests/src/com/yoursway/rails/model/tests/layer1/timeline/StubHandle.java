/**
 * 
 */
package com.yoursway.rails.model.tests.layer1.timeline;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelRoot;
import com.yoursway.model.repository.ISnapshot;
import com.yoursway.model.tracking.IMapSnapshot;

class StubHandle implements IHandle<String> {
	/**
	 * 
	 */
	private final String magicString;
	private final Class<? extends IModelRoot> modelRootClass;

	public StubHandle(String magicString, Class<? extends IModelRoot> mrc) {
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

	public Class<? extends IModelRoot> getModelRootInterface() {
		return modelRootClass;
	}

    public String resolve(ISnapshot snapshot) {
        if (snapshot instanceof IMapSnapshot) {
            IMapSnapshot mapSnapshot = (IMapSnapshot) snapshot;
            return mapSnapshot.get(this);
        }
        return null;
    }

}