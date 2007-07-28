package com.yoursway.rails.core.migrations.internal;

import com.yoursway.rails.core.internal.support.ComparingUpdater;
import com.yoursway.rails.core.migrations.IRailsMigrationsListener;
import com.yoursway.rails.core.migrations.RailsMigration;

public class RailsMigrationsBroadcastingChangeVisitor implements ComparingUpdater.IVisitor<RailsMigration> {
    
    private final Iterable<IRailsMigrationsListener> listeners;
    
    public RailsMigrationsBroadcastingChangeVisitor(Iterable<IRailsMigrationsListener> iterable) {
        this.listeners = iterable;
    }
    
    public void visitAdded(RailsMigration value) {
        for (IRailsMigrationsListener listener : listeners)
            listener.migrationAdded(value);
    }
    
    public void visitRemoved(RailsMigration value) {
        for (IRailsMigrationsListener listener : listeners)
            listener.migrationRemoved(value);
    }
    
}
