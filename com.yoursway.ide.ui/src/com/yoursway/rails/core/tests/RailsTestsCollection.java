package com.yoursway.rails.core.tests;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.core.internal.support.AbstractModel;
import com.yoursway.rails.core.projects.IProjectsListener;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.rails.core.projects.RailsProjectsCollection;

public class RailsTestsCollection extends AbstractModel<IRailsTestsListener> implements IProjectsListener,
        IRailsTestsListener {
    
    private final Map<RailsProject, PerProjectRailsTestsCollection> localModels = new HashMap<RailsProject, PerProjectRailsTestsCollection>();
    
    public RailsTestsCollection() {
        final RailsProjectsCollection projectsModel = RailsProjectsCollection.instance();
        projectsModel.addListener(this);
        for (RailsProject railsProject : projectsModel.getAll())
            projectAdded(railsProject);
    }
    
    private static final RailsTestsCollection INSTANCE = new RailsTestsCollection();
    
    public static RailsTestsCollection instance() {
        return INSTANCE;
    }
    
    public synchronized PerProjectRailsTestsCollection get(RailsProject project) {
        return localModels.get(project);
    }
    
    @Override
    protected IRailsTestsListener[] makeListenersArray(int size) {
        return new IRailsTestsListener[size];
    }
    
    public synchronized void projectAdded(RailsProject railsProject) {
        Assert.isTrue(!localModels.containsKey(railsProject));
        PerProjectRailsTestsCollection localModel = new PerProjectRailsTestsCollection(railsProject);
        add(localModel);
    }
    
    private void add(PerProjectRailsTestsCollection localModel) {
        localModels.put(localModel.getRailsProject(), localModel);
        localModel.addListener(this);
    }
    
    public synchronized void projectRemoved(RailsProject railsProject) {
        Assert.isTrue(localModels.containsKey(railsProject));
        PerProjectRailsTestsCollection oldModel = localModels.remove(railsProject);
        oldModel.removeListener(this);
    }
    
    public void reconcile(RailsProject railsProject, IResourceDelta projectDelta) {
        PerProjectRailsTestsCollection localModel = get(railsProject);
        Assert.isTrue(localModel != null);
        assert localModel != null;
        localModel.reconcile(projectDelta);
    }
    
    public void testAdded(RailsTest railsTest) {
        for (IRailsTestsListener listener : getListeners())
            listener.testAdded(railsTest);
    }
    
    public void testRemoved(RailsTest railsTest) {
        for (IRailsTestsListener listener : getListeners())
            listener.testRemoved(railsTest);
    }
    
    public void reconcile(RailsTest railsTest) {
        for (IRailsTestsListener listener : getListeners())
            listener.reconcile(railsTest);
    }
    
}
