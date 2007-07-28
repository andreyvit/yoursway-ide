/**
 * 
 */
package com.yoursway.rails.core.dbschema.internal;

import com.yoursway.rails.core.dbschema.DbTable;
import com.yoursway.rails.core.dbschema.IDbSchemaListener;
import com.yoursway.rails.core.internal.support.ComparingUpdater;

public final class BroadcastingDbTablesChangeVisitor implements ComparingUpdater.IVisitor<DbTable> {
    private final Iterable<IDbSchemaListener> listeners;
    
    public BroadcastingDbTablesChangeVisitor(Iterable<IDbSchemaListener> listeners) {
        this.listeners = listeners;
    }
    
    public void visitAdded(DbTable value) {
        for (IDbSchemaListener listener : listeners)
            listener.tableAdded(value);
    }
    
    public void visitRemoved(DbTable value) {
        for (IDbSchemaListener listener : listeners)
            listener.tableRemoved(value);
    }
    
    public void visitChanged(DbTable value) {
        for (IDbSchemaListener listener : listeners)
            listener.tableChanged(value);
    }
}