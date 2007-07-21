package com.yoursway.databinding.resources.tests.shallow;

import static org.junit.Assert.assertArrayEquals;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.junit.After;

import com.yoursway.databinding.resources.ResourceObservables;
import com.yoursway.databinding.resources.tests.ProjectTests;

public abstract class AbstractShallowChildrenTests extends ProjectTests {
    
    protected IObservableSet observable;
    protected ISetChangeListener listener;
    
    @After
    public void cleanupContainerObservingTest() {
        if (listener != null && observable != null)
            observable.removeSetChangeListener(listener);
    }
    
    protected void observe(IContainer folder) {
        observable = ResourceObservables.observeChildren(realm, folder);
    }
    
    protected void addSetChangeListener() {
        listener = context.mock(ISetChangeListener.class);
        observable.addSetChangeListener(listener);
    }
    
    protected void assertContents(final IResource... resources) {
        realm.syncExec(new Runnable() {
            
            public void run() {
                assertArrayEquals(resources, observable.toArray());
            }
            
        });
    }
    
    protected void forceRead() {
        realm.syncExec(new Runnable() {
            
            public void run() {
                observable.toArray();
            }
            
        });
    }
    
}
