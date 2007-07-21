package com.yoursway.databinding.resources.internal;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.ObservableSet;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.common.resources.IConcreteResourceChangeVisitor;
import com.yoursway.common.resources.ResourceUtils;
import com.yoursway.databinding.resources.IContainerObservableSet;

public class ShallowChildrenObservable extends ObservableSet implements IContainerObservableSet,
        IResourceChangeListener, IConcreteResourceChangeVisitor {

    private final static Iterable<IResource> NO_RESOURCES = emptyList();
    
    private final IContainer container;
    
    /**
     * Is <code>null</code> iff no listeners are registered. Otherwise is a
     * snapshot of container's children at the time the last workspace delta
     * arrivied.
     */
    private Set<IResource> children;
    
    private final class Contents extends AbstractSet<IResource> {
        
        @Override
        public Iterator<IResource> iterator() {
            try {
                final IResource[] members = container.members();
                System.out.println(members.length + " member(s) of " + container + ":");
                for (IResource member : members)
                    System.out.println("  - " + member);
                return Arrays.asList(members).iterator();
            } catch (CoreException e) {
                if (!ResourceUtils.isNotFound(e))
                    throw new AssertionError("Error " + e + " was not expected");
                return NO_RESOURCES.iterator();
            }
        }
        
        @Override
        public int size() {
            try {
                return container.members().length;
            } catch (CoreException e) {
                if (!ResourceUtils.isNotFound(e))
                    throw new AssertionError("Error " + e + " was not expected");
                return 0;
            }
        }
    }
    
    public ShallowChildrenObservable(Realm realm, IContainer container) {
        super(realm, null, IResource.class);
        setWrappedSet(new Contents());
        this.container = container;
    }
    
    public IContainer getContainer() {
        return container;
    }
    
    @Override
    protected void firstListenerAdded() {
        final boolean isStale = obtainActualChildrenReturnStaleFlag();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
        getRealm().exec(new Runnable() {
            
            public void run() {
                setStale(isStale);
            }
        });
    }
    
    private boolean obtainActualChildrenReturnStaleFlag() {
        boolean isStale = false;
        try {
            children = new HashSet<IResource>(asList(container.members()));
        } catch (CoreException e) {
            if (ResourceUtils.isOutOfSync(e))
                isStale = true;
            children = new HashSet<IResource>();
        }
        return isStale;
    }
    
    @Override
    protected void lastListenerRemoved() {
        children = null;
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
    }
    
    public void resourceChanged(IResourceChangeEvent event) {
        final IResourceDelta delta = event.getDelta();
        if (delta != null)
            ResourceUtils.interpretDelta(delta, container.getFullPath(), ShallowChildrenObservable.this);
    }
    
    private void fire() {
        getRealm().exec(new Runnable() {
            
            public void run() {
                // listeners might have been removed
                if (children == null)
                    return;
                Set<IResource> oldChildren = new HashSet<IResource>(children);
                boolean isStale = obtainActualChildrenReturnStaleFlag();
                setStale(isStale);
                if (!isStale && !oldChildren.equals(children))
                    fireSetChange(Diffs.computeSetDiff(oldChildren, children));
            }
            
        });
    }
    
    public void resourceChanged(IResourceDelta delta) {
        fire();
    }
    
    public void resourceMightHaveBeenAdded() {
        fire();
    }
    
    public void resourceRemovedIfExisted() {
        fire();
    }
    
}
