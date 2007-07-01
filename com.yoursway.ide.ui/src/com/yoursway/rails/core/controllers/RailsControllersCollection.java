package com.yoursway.rails.core.controllers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.core.internal.support.AbstractModel;
import com.yoursway.rails.core.projects.IProjectsListener;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.rails.core.projects.RailsProjectsCollection;

public class RailsControllersCollection extends AbstractModel<IRailsControllersListener> implements IProjectsListener,
        IRailsControllersListener {
    
    private final Map<RailsProject, PerProjectRailsControllersCollection> localModels = new HashMap<RailsProject, PerProjectRailsControllersCollection>();
    
    public RailsControllersCollection() {
        final RailsProjectsCollection projectsModel = RailsProjectsCollection.getInstance();
        projectsModel.addListener(this);
        for (RailsProject railsProject : projectsModel.getAll())
            projectAdded(railsProject);
    }
    
    private static final RailsControllersCollection INSTANCE = new RailsControllersCollection();
    
    public static RailsControllersCollection getInstance() {
        return INSTANCE;
    }
    
    public synchronized PerProjectRailsControllersCollection get(RailsProject project) {
        return localModels.get(project);
    }
    
    @Override
    protected IRailsControllersListener[] makeListenersArray(int size) {
        return new IRailsControllersListener[size];
    }
    
    public synchronized void projectAdded(RailsProject railsProject) {
        Assert.isTrue(!localModels.containsKey(railsProject));
        PerProjectRailsControllersCollection localModel = new PerProjectRailsControllersCollection(railsProject);
        add(localModel);
    }
    
    private void add(PerProjectRailsControllersCollection localModel) {
        localModels.put(localModel.getRailsProject(), localModel);
        localModel.addListener(this);
    }
    
    public synchronized void projectRemoved(RailsProject railsProject) {
        Assert.isTrue(localModels.containsKey(railsProject));
        PerProjectRailsControllersCollection oldModel = localModels.remove(railsProject);
        oldModel.removeListener(this);
    }
    
    public void reconcile(RailsProject railsProject, IResourceDelta projectDelta) {
        PerProjectRailsControllersCollection localModel = get(railsProject);
        Assert.isTrue(localModel != null);
        assert localModel != null;
        localModel.reconcile(projectDelta);
    }
    
    public void controllerAdded(RailsController railsController) {
        for (IRailsControllersListener listener : getListeners())
            listener.controllerAdded(railsController);
    }
    
    public void controllerRemoved(RailsController railsController) {
        for (IRailsControllersListener listener : getListeners())
            listener.controllerRemoved(railsController);
    }
    
    public void reconcile(RailsController railsController) {
        for (IRailsControllersListener listener : getListeners())
            listener.reconcile(railsController);
    }
    
}
