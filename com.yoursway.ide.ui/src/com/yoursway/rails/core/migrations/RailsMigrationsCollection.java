package com.yoursway.rails.core.migrations;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.core.internal.support.AbstractModel;
import com.yoursway.rails.core.projects.IProjectsListener;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.rails.core.projects.RailsProjectsCollection;

public class RailsMigrationsCollection extends AbstractModel<IRailsMigrationsListener> implements
        IProjectsListener, IRailsMigrationsListener {
    
    private final Map<RailsProject, PerProjectRailsMigrationsCollection> localModels = new HashMap<RailsProject, PerProjectRailsMigrationsCollection>();
    
    public RailsMigrationsCollection() {
        final RailsProjectsCollection projectsModel = RailsProjectsCollection.instance();
        projectsModel.addListener(this);
        for (RailsProject railsProject : projectsModel.getAll())
            projectAdded(railsProject);
    }
    
    private static final RailsMigrationsCollection INSTANCE = new RailsMigrationsCollection();
    
    public static RailsMigrationsCollection instance() {
        return INSTANCE;
    }
    
    public synchronized PerProjectRailsMigrationsCollection get(RailsProject project) {
        return localModels.get(project);
    }
    
    @Override
    protected IRailsMigrationsListener[] makeListenersArray(int size) {
        return new IRailsMigrationsListener[size];
    }
    
    public synchronized void projectAdded(RailsProject railsProject) {
        Assert.isTrue(!localModels.containsKey(railsProject));
        add(new PerProjectRailsMigrationsCollection(railsProject));
    }
    
    private void add(PerProjectRailsMigrationsCollection localModel) {
        localModels.put(localModel.getRailsProject(), localModel);
        localModel.addListener(this);
    }
    
    public synchronized void projectRemoved(RailsProject railsProject) {
        Assert.isTrue(localModels.containsKey(railsProject));
        PerProjectRailsMigrationsCollection oldModel = localModels.remove(railsProject);
        oldModel.removeListener(this);
    }
    
    public void reconcile(RailsProject railsProject, IResourceDelta projectDelta) {
        PerProjectRailsMigrationsCollection localModel = get(railsProject);
        Assert.isTrue(localModel != null);
        assert localModel != null;
        localModel.reconcile(projectDelta);
    }
    
    public void migrationAdded(RailsMigration subject) {
        for (IRailsMigrationsListener listener : getListeners())
            listener.migrationAdded(subject);
    }
    
    public void migrationContentChanged(RailsMigration subject) {
        for (IRailsMigrationsListener listener : getListeners())
            listener.migrationContentChanged(subject);
    }
    
    public void migrationRemoved(RailsMigration subject) {
        for (IRailsMigrationsListener listener : getListeners())
            listener.migrationRemoved(subject);
    }
    
}
