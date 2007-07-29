package com.yoursway.databinding.resources.internal;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.set.ObservableSet;
import org.eclipse.core.databinding.observable.set.SetDiff;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.common.resources.IConcreteResourceChangeVisitor;
import com.yoursway.common.resources.ResourceUtils;
import com.yoursway.databinding.commons.YourSwayRealm;
import com.yoursway.databinding.resources.IContainerObservableSet;

public class ShallowChildrenObservable extends ObservableSet implements IContainerObservableSet,
        IResourceChangeListener {
    
    final static Iterable<IResource> NO_RESOURCES = emptyList();
    private final IContainer container;
    
    public ShallowChildrenObservable(YourSwayRealm realm, IContainer container) {
        super(realm, null, IResource.class);
        this.container = container;
        setWrappedSet(new ShallowChildrenSet(container));
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }
    
    @Override
    public synchronized void dispose() {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
    }
    
    public void resourceChanged(IResourceChangeEvent event) {
        final IResourceDelta delta = event.getDelta();
        if (delta != null && ResourceUtils.changedInDelta(delta, container.getFullPath())
            fire();
    }
    
    private void fire() {
        getRealm().exec(new Runnable() {
            
            public void run() {
                fireSetChange(new SetDiff() {
                    
                    @Override
                    public Set<?> getAdditions() {
                        throw new UnsupportedOperationException();
                    }
                    
                    @Override
                    public Set<?> getRemovals() {
                        throw new UnsupportedOperationException();
                    }
                    
                });
            }
            
        });
    }
    
}
