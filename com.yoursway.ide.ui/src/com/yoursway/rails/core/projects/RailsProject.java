package com.yoursway.rails.core.projects;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.commons.Inflector;
import com.yoursway.rails.commons.RailsNamingConventions;

public class RailsProject {
    
    private final IProject project;
    private final Inflector inflector;
    
    public RailsProject(IProject project) {
        Assert.isLegal(project != null);
        this.project = project;
        inflector = RailsNamingConventions.createInitializedInflector();
    }
    
    public IProject getProject() {
        return project;
    }
    
    public Inflector getInflector() {
        return inflector;
    }
    
}
