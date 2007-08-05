package com.yoursway.tests.commons;

import static org.junit.Assert.assertArrayEquals;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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
    
    protected void forceRead() throws Exception {
        FutureTask<Object> futureTask = new FutureTask<Object>(new Runnable() {
            
            public void run() {
                observable.toArray();
            }
            
        }, null);
        realm.asyncExec(futureTask);
        try {
            futureTask.get();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Exception)
                throw (Exception) cause;
            else
                throw (Error) cause;
        }
    }
    
}
