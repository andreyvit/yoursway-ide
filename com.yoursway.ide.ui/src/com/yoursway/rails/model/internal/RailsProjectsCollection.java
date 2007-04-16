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

public class RailsProjectsCollection extends RailsElement implements IRailsProjectsCollection {
    
    private final IWorkspaceRoot workspaceRoot;
    
    private final Map<IProject, RailsProject> projects = new HashMap<IProject, RailsProject>();
    
    private boolean projectsKnown = false;
    
    public RailsProjectsCollection(IWorkspaceRoot workspaceRoot) {
        this.workspaceRoot = workspaceRoot;
    }
    
    public Collection<? extends IRailsProject> getRailsProjects() {
        ensureAllKnown();
        return projects.values();
    }
    
    private void ensureAllKnown() {
        if (!projectsKnown)
            refresh();
    }
    
    public void refresh() {
        projects.clear();
        for (IProject project : workspaceRoot.getProjects()) {
            RailsProject railsProject = new RailsProject(this, project);
            projects.put(project, railsProject);
        }
        projectsKnown = true;
    }
    
    public void reconcile(RailsDeltaBuilder deltaBuilder, IResourceDelta parentDelta) {
        for (IResourceDelta delta : parentDelta.getAffectedChildren()) {
            IResource resource = delta.getResource();
            if (resource.getType() == IResource.PROJECT) {
                IProject project = (IProject) resource;
                switch (delta.getKind()) {
                case IResourceDelta.ADDED:
                    addProject(project);
                    deltaBuilder.somethingChanged();
                    break;
                case IResourceDelta.REMOVED:
                    removeProject(project);
                    deltaBuilder.somethingChanged();
                    break;
                case IResourceDelta.CHANGED:
                    RailsProject railsProject = getProject(project);
                    deltaBuilder.somethingChanged();
                    railsProject.reconcile(deltaBuilder, delta);
                    break;
                }
            }
        }
    }
    
    private void addProject(IProject project) {
        projects.put(project, new RailsProject(this, project));
    }
    
    private RailsProject getProject(IProject project) {
        RailsProject railsProject = projects.get(project);
        if (railsProject == null && !projectsKnown) {
            
        }
        return railsProject;
    }
    
    private void removeProject(IProject project) {
        projects.remove(project);
    }
}
