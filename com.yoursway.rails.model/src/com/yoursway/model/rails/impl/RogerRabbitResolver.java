package com.yoursway.model.rails.impl;

import java.util.Collection;

import com.yoursway.model.repository.DependencyRequestor;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelRoot;
import com.yoursway.model.repository.IModelRootProvider;
import com.yoursway.model.repository.IResolver;

public class RogerRabbitResolver implements IResolver {

	private final IModelRootProvider modelRootProvider;
	private final DependencyRequestor dependencyRequestor;

	public RogerRabbitResolver(IModelRootProvider modelRootProvider,
			DependencyRequestor dependencyRequestor) {
		this.modelRootProvider = modelRootProvider;
		this.dependencyRequestor = dependencyRequestor;
	}

	public <V> Collection<? extends V> changedHandles(Class<V> handleInterface) {
		return null;
	}

	public void checkCancellation() {
	}

	public <V, H extends IHandle<V>> V get(H handle) {
		dependencyRequestor.dependency(handle);
		if (handle instanceof RabbitHandle) {
			RabbitHandle<V> h = (RabbitHandle<V>) handle;
			return h.getData();
		}
		if (handle instanceof RabbitFamilyHandle) {
			RabbitFamilyHandle<V> familyHandle = (RabbitFamilyHandle<V>) handle;
			return (V) familyHandle.getData();
		}
		return null;
	}

	public <V, H extends IHandle<V>> V getIfAvail(H handle) {
		dependencyRequestor.dependency(handle);
		if (handle instanceof RabbitHandle) {
			RabbitHandle<V> h = (RabbitHandle<V>) handle;
			return h.getData();
		}
		return null;
	}

	public <V extends IModelRoot> V obtainRoot(Class<V> rootHandleInterface) {
		return modelRootProvider.obtainRoot(rootHandleInterface);
	}

	public Collection<? extends IHandle<?>> changedHandlesForModel(
			Class<?> rootHandleInterface) {
		// TODO Auto-generated method stub
		return null;
	}

	public void dontKillForLaterAccess() {
		// TODO Auto-generated method stub

	}

}
