package com.yoursway.tests.commons;

import static org.junit.Assert.assertArrayEquals;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;

public class AbstractObservableSetTestCase<T> extends ProjectTests {
    
    protected ISetChangeListener listener;
    
    protected IObservableSet observable;
    
    protected void addSetChangeListener() {
        listener = context.mock(ISetChangeListener.class);
        observable.addSetChangeListener(listener);
    }
    
    protected void assertContents(final T... items) {
        realm.asyncExec(new Runnable() {
            
            public void run() {
                assertArrayEquals(items, observable.toArray());
            }
            
        });
        realm.runAsyncTasks();
    }
    
    protected void forceRead() {
        realm.syncExec(new Runnable() {
            
            public void run() {
                observable.toArray();
            }
            
        });
    }
    
}
