package com.yoursway.rails.core.fixtures;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.core.internal.support.AbstractModel;
import com.yoursway.rails.core.projects.IProjectsListener;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.rails.core.projects.RailsProjectsCollection;

public class RailsFixturesCollection extends AbstractModel<IRailsFixturesListener> implements
        IProjectsListener, IRailsFixturesListener {
    
    private final Map<RailsProject, PerProjectRailsFixturesCollection> localModels = new HashMap<RailsProject, PerProjectRailsFixturesCollection>();
    
    public RailsFixturesCollection() {
        final RailsProjectsCollection projectsModel = RailsProjectsCollection.instance();
        projectsModel.addListener(this);
        for (RailsProject railsProject : projectsModel.getAll())
            projectAdded(railsProject);
    }
    
    private static final RailsFixturesCollection INSTANCE = new RailsFixturesCollection();
    
    public static RailsFixturesCollection instance() {
        return INSTANCE;
    }
    
    public synchronized PerProjectRailsFixturesCollection get(RailsProject project) {
        return localModels.get(project);
    }
    
    @Override
    protected IRailsFixturesListener[] makeListenersArray(int size) {
        return new IRailsFixturesListener[size];
    }
    
    public synchronized void projectAdded(RailsProject railsProject) {
        Assert.isTrue(!localModels.containsKey(railsProject));
        PerProjectRailsFixturesCollection localModel = new PerProjectRailsFixturesCollection(railsProject);
        add(localModel);
    }
    
    private void add(PerProjectRailsFixturesCollection localModel) {
        localModels.put(localModel.getRailsProject(), localModel);
        localModel.addListener(this);
    }
    
    public synchronized void projectRemoved(RailsProject railsProject) {
        Assert.isTrue(localModels.containsKey(railsProject));
        PerProjectRailsFixturesCollection oldModel = localModels.remove(railsProject);
        oldModel.removeListener(this);
    }
    
    public void reconcile(RailsProject railsProject, IResourceDelta projectDelta) {
        PerProjectRailsFixturesCollection localModel = get(railsProject);
        Assert.isTrue(localModel != null);
        assert localModel != null;
        localModel.reconcile(projectDelta);
    }
    
    public void fixtureAdded(RailsFixture railsFixture) {
        for (IRailsFixturesListener listener : getListeners())
            listener.fixtureAdded(railsFixture);
    }
    
    public void fixtureRemoved(RailsFixture railsFixture) {
        for (IRailsFixturesListener listener : getListeners())
            listener.fixtureRemoved(railsFixture);
    }
    
    public void reconcile(RailsFixture railsFixture) {
        for (IRailsFixturesListener listener : getListeners())
            listener.reconcile(railsFixture);
    }
    
}
