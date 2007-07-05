/**
 * 
 */
package com.yoursway.rails.core.fixtures.internal;

import com.yoursway.rails.core.fixtures.IRailsFixturesListener;
import com.yoursway.rails.core.fixtures.RailsFixture;
import com.yoursway.rails.core.internal.support.ComparingUpdater;

public final class BroadcastingRailsFixturesChangeVisitor implements ComparingUpdater.IVisitor<RailsFixture> {
    private final IRailsFixturesListener[] listeners;
    
    public BroadcastingRailsFixturesChangeVisitor(IRailsFixturesListener[] listeners) {
        this.listeners = listeners;
    }
    
    public void visitAdded(RailsFixture value) {
        for (IRailsFixturesListener listener : listeners)
            listener.fixtureAdded(value);
    }
    
    public void visitRemoved(RailsFixture value) {
        for (IRailsFixturesListener listener : listeners)
            listener.fixtureRemoved(value);
    }
}