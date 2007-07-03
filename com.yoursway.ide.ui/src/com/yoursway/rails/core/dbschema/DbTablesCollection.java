package com.yoursway.rails.core.dbschema;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.core.internal.support.AbstractModel;
import com.yoursway.rails.core.projects.IProjectsListener;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.rails.core.projects.RailsProjectsCollection;

public class DbTablesCollection extends AbstractModel<IDbSchemaListener> implements IProjectsListener,
        IDbSchemaListener {
    
    private final Map<RailsProject, PerProjectDbTablesCollection> localModels = new HashMap<RailsProject, PerProjectDbTablesCollection>();
    
    public DbTablesCollection() {
        final RailsProjectsCollection projectsModel = RailsProjectsCollection.getInstance();
        projectsModel.addListener(this);
        for (RailsProject railsProject : projectsModel.getAll())
            projectAdded(railsProject);
    }
    
    private static final DbTablesCollection INSTANCE = new DbTablesCollection();
    
    public static DbTablesCollection getInstance() {
        return INSTANCE;
    }
    
    public synchronized PerProjectDbTablesCollection get(RailsProject project) {
        return localModels.get(project);
    }
    
    @Override
    protected IDbSchemaListener[] makeListenersArray(int size) {
        return new IDbSchemaListener[size];
    }
    
    public synchronized void projectAdded(RailsProject railsProject) {
        Assert.isTrue(!localModels.containsKey(railsProject));
        PerProjectDbTablesCollection localModel = new PerProjectDbTablesCollection(railsProject);
        add(localModel);
    }
    
    private void add(PerProjectDbTablesCollection localModel) {
        localModels.put(localModel.getRailsProject(), localModel);
        localModel.addListener(this);
    }
    
    public synchronized void projectRemoved(RailsProject railsProject) {
        Assert.isTrue(localModels.containsKey(railsProject));
        PerProjectDbTablesCollection oldModel = localModels.remove(railsProject);
        oldModel.removeListener(this);
    }
    
    public void reconcile(RailsProject railsProject, IResourceDelta projectDelta) {
        PerProjectDbTablesCollection localModel = get(railsProject);
        Assert.isTrue(localModel != null);
        assert localModel != null;
        localModel.reconcile(projectDelta);
    }
    
    public void tableAdded(DbTable dbTable) {
        for (IDbSchemaListener listener : getListeners())
            listener.tableAdded(dbTable);
    }
    
    public void tableChanged(DbTable dbTable) {
        for (IDbSchemaListener listener : getListeners())
            listener.tableChanged(dbTable);
    }
    
    public void tableRemoved(DbTable dbTable) {
        for (IDbSchemaListener listener : getListeners())
            listener.tableRemoved(dbTable);
    }
    
}
