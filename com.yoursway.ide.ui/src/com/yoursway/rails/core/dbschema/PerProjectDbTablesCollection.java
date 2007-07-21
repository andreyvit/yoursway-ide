package com.yoursway.rails.core.dbschema;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResourceDelta;

import com.yoursway.common.resources.ResourceUtils;
import com.yoursway.rails.core.dbschema.internal.BroadcastingDbTablesChangeVisitor;
import com.yoursway.rails.core.dbschema.internal.DbSchemaIterator;
import com.yoursway.rails.core.dbschema.internal.DbTablesRequestor;
import com.yoursway.rails.core.internal.support.AbstractModel;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.utils.RailsNamingConventions;

public class PerProjectDbTablesCollection extends AbstractModel<IDbSchemaListener> {
    
    private Map<String, DbTable> items = new HashMap<String, DbTable>();
    private final RailsProject railsProject;
    
    public PerProjectDbTablesCollection(RailsProject railsProject) {
        this.railsProject = railsProject;
        rebuild();
    }
    
    public RailsProject getRailsProject() {
        return railsProject;
    }
    
    public Collection<DbTable> getAll() {
        return items.values();
    }
    
    public DbTable get(String name) {
        return items.get(name);
    }
    
    public void rebuild() {
        DbTablesRequestor updater = new DbTablesRequestor(railsProject, items);
        new DbSchemaIterator(railsProject, updater).build();
        items = updater.getNewItems();
        updater.visitChanges(new BroadcastingDbTablesChangeVisitor(getListeners()));
    }
    
    @Override
    protected IDbSchemaListener[] makeListenersArray(int size) {
        return new IDbSchemaListener[size];
    }
    
    public void reconcile(IResourceDelta projectRD) {
        if (!ResourceUtils.changedInDelta(projectRD, RailsNamingConventions.DB_SCHEMA_RB_PATH))
            return;
        rebuild();
    }
}
