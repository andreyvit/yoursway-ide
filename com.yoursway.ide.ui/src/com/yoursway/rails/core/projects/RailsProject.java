package com.yoursway.rails.core.projects;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;

public class RailsProject {
    
    private final IProject project;
    
    public RailsProject(IProject project) {
        Assert.isLegal(project != null);
        this.project = project;
    }
    
    public IProject getProject() {
        return project;
    }
    
}
