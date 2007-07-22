package com.yoursway.tests.commons;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TestFriendlyLockBasedRealm extends LockBasedRealm {
    
    private Queue<Runnable> asyncRunnables = new ConcurrentLinkedQueue<Runnable>();
    
    @Override
    public void asyncExec(Runnable runnable) {
        asyncRunnables.add(runnable);
    }
    
    public void runAsyncTasks() {
        Runnable runnable;
        while ((runnable = asyncRunnables.poll()) != null)
            syncExec(runnable);
    }
    
}
