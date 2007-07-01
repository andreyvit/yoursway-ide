package com.yoursway.rails.core.models;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.Assert;

import com.yoursway.core.internal.support.AbstractModel;
import com.yoursway.rails.core.projects.IProjectsListener;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.rails.core.projects.RailsProjectsCollection;

public class RailsModelsCollection extends AbstractModel<IModelsListener> implements IProjectsListener,
        IModelsListener {
    
    private final Map<RailsProject, PerProjectRailsModelsCollection> localModels = new HashMap<RailsProject, PerProjectRailsModelsCollection>();
    
    public RailsModelsCollection() {
        final RailsProjectsCollection projectsModel = RailsProjectsCollection.getInstance();
        projectsModel.addListener(this);
        for (RailsProject railsProject : projectsModel.getAll())
            projectAdded(railsProject);
    }
    
    private static final RailsModelsCollection INSTANCE = new RailsModelsCollection();
    
    public static RailsModelsCollection getInstance() {
        return INSTANCE;
    }
    
    public synchronized PerProjectRailsModelsCollection get(RailsProject project) {
        return localModels.get(project);
    }
    
    @Override
    protected IModelsListener[] makeListenersArray(int size) {
        return new IModelsListener[size];
    }
    
    public synchronized void projectAdded(RailsProject railsProject) {
        Assert.isTrue(!localModels.containsKey(railsProject));
        PerProjectRailsModelsCollection localModel = new PerProjectRailsModelsCollection(railsProject);
        add(localModel);
    }
    
    private void add(PerProjectRailsModelsCollection localModel) {
        localModels.put(localModel.getRailsProject(), localModel);
        localModel.addListener(this);
    }
    
    public synchronized void projectRemoved(RailsProject railsProject) {
        Assert.isTrue(localModels.containsKey(railsProject));
        PerProjectRailsModelsCollection oldModel = localModels.remove(railsProject);
        oldModel.removeListener(this);
    }
    
    public void reconcile(RailsProject railsProject, IResourceDelta projectDelta) {
        PerProjectRailsModelsCollection localModel = get(railsProject);
        Assert.isTrue(localModel != null);
        assert localModel != null;
        localModel.reconcile(projectDelta);
    }
    
    public void modelAdded(RailsModel railsModel) {
        for (IModelsListener listener : getListeners())
            listener.modelAdded(railsModel);
    }
    
    public void modelRemoved(RailsModel railsModel) {
        for (IModelsListener listener : getListeners())
            listener.modelRemoved(railsModel);
    }
    
    public void modelContentChanged(RailsModel railsModel) {
        for (IModelsListener listener : getListeners())
            listener.modelContentChanged(railsModel);
    }
    
}
