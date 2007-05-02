package com.yoursway.rails.model.internal.infos;

import org.eclipse.core.resources.IProject;

public class ProjectInfo {
    
    private final IProject project;
    
    public ProjectInfo(IProject project) {
        this.project = project;
    }
    
    public IProject getProject() {
        return project;
    }
    
}
