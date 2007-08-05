/**
 * 
 */
package com.yoursway.databinding.commons;

import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.databinding.observable.Realm;

public class YourSwayRealm extends Realm {
    private ReentrantLock lock = new ReentrantLock();
    
    @Override
    public boolean isCurrent() {
        return lock.isHeldByCurrentThread();
    }
    
    @Override
    protected void syncExec(Runnable runnable) {
        lock.lock();
        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void asyncExec(Runnable runnable) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void exec(Runnable runnable) {
        syncExec(runnable);
    }
    
}