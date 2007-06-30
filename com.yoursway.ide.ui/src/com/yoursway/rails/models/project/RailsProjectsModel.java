package com.yoursway.rails.models.project;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;

import com.yoursway.rails.models.AbstractModel;
import com.yoursway.rails.models.project.internal.BroadcastingChangeVisitor;
import com.yoursway.rails.models.project.internal.RailsProjectsIterator;
import com.yoursway.rails.models.project.internal.Requestor;
import com.yoursway.rails.models.rails.IRailsListener;
import com.yoursway.rails.models.rails.Rails;

public class RailsProjectsModel extends AbstractModel<IProjectsListener> implements IRailsListener {
    
    private Map<IProject, RailsProject> projects = new HashMap<IProject, RailsProject>();
    
    private static final RailsProjectsModel INSTANCE = new RailsProjectsModel();
    
    public static RailsProjectsModel getInstance() {
        return INSTANCE;
    }
    
    public RailsProjectsModel() {
        Rails.getInstance().addListener(this);
        rebuild();
    }
    
    public Collection<RailsProject> getAll() {
        return projects.values();
    }
    
    public RailsProject get(IProject project) {
        return projects.get(project);
    }
    
    public void rebuild() {
        Requestor updater = new Requestor(projects);
        new RailsProjectsIterator(updater).build();
        projects = updater.getNewItems();
        updater.visitChanges(new BroadcastingChangeVisitor(getListeners()));
    }
    
    @Override
    protected IProjectsListener[] makeListenersArray(int size) {
        return new IProjectsListener[size];
    }
    
    public void reconcile(IResourceDelta workspaceRD) {
        rebuild();
        IResourceDelta[] children = workspaceRD.getAffectedChildren(IResourceDelta.CHANGED);
        for (IResourceDelta projectResourceDelta : children) {
            IProject project = (IProject) projectResourceDelta.getResource();
            RailsProject railsProject = projects.get(project);
            if (railsProject == null)
                continue; // not a Rails project
            for (IProjectsListener listener : getListeners())
                listener.reconcile(railsProject, projectResourceDelta);
        }
    }
}
