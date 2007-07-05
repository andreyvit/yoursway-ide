/**
 * 
 */
package com.yoursway.rails.core.tests.internal;

import com.yoursway.rails.core.internal.support.ComparingUpdater;
import com.yoursway.rails.core.tests.IRailsTestsListener;
import com.yoursway.rails.core.tests.RailsTest;

public final class BroadcastingRailsTestsChangeVisitor implements ComparingUpdater.IVisitor<RailsTest> {
    private final IRailsTestsListener[] listeners;
    
    public BroadcastingRailsTestsChangeVisitor(IRailsTestsListener[] listeners) {
        this.listeners = listeners;
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