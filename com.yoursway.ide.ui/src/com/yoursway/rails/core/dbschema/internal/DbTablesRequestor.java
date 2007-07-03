/**
 * 
 */
package com.yoursway.rails.core.dbschema.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.yoursway.rails.core.dbschema.DbTable;
import com.yoursway.rails.core.internal.support.ComparingUpdater;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.rails.utils.schemaparser.TableInfo;

public final class DbTablesRequestor extends ComparingUpdater<TableInfo, String, DbTable> implements
        IDbTablesRequestor {
    
    private final RailsProject railsProject;
    
    private final Collection<DbTable> changedTables = new ArrayList<DbTable>();
    
    public DbTablesRequestor(RailsProject railsProject, Map<String, DbTable> oldItems) {
        super(oldItems);
        this.railsProject = railsProject;
    }
    
    @Override
    protected DbTable create(TableInfo data) {
        return new DbTable(railsProject, data);
    }
    
    @Override
    protected String getKey(TableInfo data) {
        return data.name;
    }
    
    @Override
    protected void update(DbTable value, TableInfo data) {
        if (value.update(data.fields))
            changedTables.add(value);
    }
    
    public void visitChanges(BroadcastingDbTablesChangeVisitor visitor) {
        super.visitChanges(visitor);
        for (DbTable table : changedTables)
            visitor.visitChanged(table);
    }
    
}