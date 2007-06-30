package com.yoursway.rails.models.controller.all;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.models.AbstractModel;
import com.yoursway.rails.models.controller.IControllersListener;
import com.yoursway.rails.models.controller.RailsController;
import com.yoursway.rails.models.controller.RailsControllers;
import com.yoursway.rails.models.project.IProjectsListener;
import com.yoursway.rails.models.project.RailsProject;
import com.yoursway.rails.models.project.RailsProjectsModel;

public class RailsControllersModel extends AbstractModel<IControllersListener> implements IProjectsListener,
        IControllersListener {
    
    private final Map<RailsProject, RailsControllers> localModels = new HashMap<RailsProject, RailsControllers>();
    
    public RailsControllersModel() {
        final RailsProjectsModel projectsModel = RailsProjectsModel.getInstance();
        projectsModel.addListener(this);
        for (RailsProject railsProject : projectsModel.getAll())
            projectAdded(railsProject);
    }
    
    private static final RailsControllersModel INSTANCE = new RailsControllersModel();
    
    public static RailsControllersModel getInstance() {
        return INSTANCE;
    }
    
    public synchronized RailsControllers get(RailsProject project) {
        return localModels.get(project);
    }
    
    @Override
    protected IControllersListener[] makeListenersArray(int size) {
        return new IControllersListener[size];
    }
    
    public synchronized void projectAdded(RailsProject railsProject) {
        Assert.isTrue(!localModels.containsKey(railsProject));
        RailsControllers localModel = new RailsControllers(railsProject);
        add(localModel);
    }
    
    private void add(RailsControllers localModel) {
        localModels.put(localModel.getRailsProject(), localModel);
        localModel.addListener(this);
    }
    
    public synchronized void projectRemoved(RailsProject railsProject) {
        Assert.isTrue(localModels.containsKey(railsProject));
        RailsControllers oldModel = localModels.remove(railsProject);
        oldModel.removeListener(this);
    }
    
    public void reconcile(RailsProject railsProject, IResourceDelta projectDelta) {
        RailsControllers localModel = get(railsProject);
        Assert.isTrue(localModel != null);
        assert localModel != null;
        localModel.reconcile(projectDelta);
    }
    
    public void controllerAdded(RailsController railsController) {
        for (IControllersListener listener : getListeners())
            listener.controllerAdded(railsController);
    }
    
    public void controllerRemoved(RailsController railsController) {
        for (IControllersListener listener : getListeners())
            listener.controllerRemoved(railsController);
    }
    
    public void reconcile(RailsController railsController) {
        for (IControllersListener listener : getListeners())
            listener.reconcile(railsController);
    }
    
}
