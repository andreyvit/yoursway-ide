package com.yoursway.rails.models.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;

import com.yoursway.rails.models.AbstractModel;
import com.yoursway.rails.models.project.IProjectsListener;
import com.yoursway.rails.models.project.RailsProject;
import com.yoursway.rails.models.project.internal.BroadcastingChangeVisitor;
import com.yoursway.rails.models.project.internal.RailsProjectsIterator;
import com.yoursway.rails.models.project.internal.Requestor;
import com.yoursway.rails.models.rails.IRailsListener;

public class RailsControllersModel extends AbstractModel<IProjectsListener> implements IRailsListener {
    
    private Map<IProject, RailsProject> projects = new HashMap<IProject, RailsProject>();
    
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
