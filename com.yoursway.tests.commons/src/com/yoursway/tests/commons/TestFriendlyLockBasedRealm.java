package com.yoursway.tests.commons;

import java.util.LinkedList;
import java.util.Queue;

public class TestFriendlyLockBasedRealm extends LockBasedRealm {
    
    private Queue<Runnable> asyncRunnables = new LinkedList<Runnable>();
    
    @Override
    public void asyncExec(Runnable runnable) {
        asyncRunnables.add(runnable);
    }
    
    public void runAsyncTasks() {
        for (Runnable runnable : asyncRunnables)
            syncExec(runnable);
    }
    
}
