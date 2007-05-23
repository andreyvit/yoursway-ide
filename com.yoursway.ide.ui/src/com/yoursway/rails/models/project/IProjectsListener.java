package com.yoursway.rails.models.project;

import org.eclipse.core.resources.IResourceDelta;

public interface IProjectsListener {
    
    void projectAdded(RailsProject railsProject);
    
    void projectRemoved(RailsProject railsProject);
    
    void reconcile(RailsProject railsProject, IResourceDelta resourceDelta);
    
}
