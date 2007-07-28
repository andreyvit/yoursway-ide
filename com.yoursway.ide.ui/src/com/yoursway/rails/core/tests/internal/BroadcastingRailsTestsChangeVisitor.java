/**
 * 
 */
package com.yoursway.rails.core.tests.internal;

import com.yoursway.rails.core.internal.support.ComparingUpdater;
import com.yoursway.rails.core.tests.IRailsTestsListener;
import com.yoursway.rails.core.tests.RailsTest;

public final class BroadcastingRailsTestsChangeVisitor implements ComparingUpdater.IVisitor<RailsTest> {
    private final Iterable<IRailsTestsListener> listeners;
    
    public BroadcastingRailsTestsChangeVisitor(Iterable<IRailsTestsListener> iterable) {
        this.listeners = iterable;
    }
    
    public void visitAdded(RailsTest value) {
        for (IRailsTestsListener listener : listeners)
            listener.testAdded(value);
    }
    
    public void visitRemoved(RailsTest value) {
        for (IRailsTestsListener listener : listeners)
            listener.testRemoved(value);
    }
}