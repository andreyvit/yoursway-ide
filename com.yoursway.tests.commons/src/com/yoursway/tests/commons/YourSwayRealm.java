/**
 * 
 */
package com.yoursway.tests.commons;

import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.databinding.observable.Realm;

public class LockBasedRealm extends Realm {
    private ReentrantLock lock = new ReentrantLock();
    
    @Override
    public boolean isCurrent() {
        return lock.isHeldByCurrentThread();
    }
    
    @Override
    public void syncExec(Runnable runnable) {
        lock.lock();
        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }
}