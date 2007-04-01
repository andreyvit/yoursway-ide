package com.yoursway.rails.model.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;

import com.yoursway.rails.model.IRailsProject;
import com.yoursway.rails.model.IRailsProjectsCollection;

public class RailsProjectsCollection implements IRailsProjectsCollection {
    
    private final IWorkspaceRoot workspaceRoot;
    
    private final Map<IProject, RailsProject> projects = new HashMap<IProject, RailsProject>();
    
    public RailsProjectsCollection(IWorkspaceRoot workspaceRoot) {
        this.workspaceRoot = workspaceRoot;
        refresh();
    }
    
    public Collection<? extends IRailsProject> getRailsProjects() {
        return projects.values();
    }
    
    public void refresh() {
        projects.clear();
        for (IProject project : workspaceRoot.getProjects()) {
            RailsProject railsProject = new RailsProject(this, project);
            projects.put(project, railsProject);
        }
    }
    
    public void reconcile(RailsDeltaBuilder deltaBuilder, IResourceDelta parentDelta) {
        for (IResourceDelta delta : parentDelta.getAffectedChildren()) {
            IResource resource = delta.getResource();
            if (resource.getType() == IResource.PROJECT) {
                IProject project = (IProject) resource;
                switch (delta.getKind()) {
                case IResourceDelta.ADDED:
                    projects.put(project, new RailsProject(this, project));
                    deltaBuilder.somethingChanged();
                    break;
                case IResourceDelta.REMOVED:
                    projects.remove(project);
                    deltaBuilder.somethingChanged();
                    break;
                case IResourceDelta.CHANGED:
                    RailsProject railsProject = projects.get(project);
                    deltaBuilder.somethingChanged();
                    railsProject.reconcile(deltaBuilder, delta);
                    break;
                }
            }
        }
    }
}
