package com.yoursway.rails.core.projects.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;

public class RailsProjectsIterator {
    
    private final IRailsProjectsRequestor requestor;
    
    public RailsProjectsIterator(IRailsProjectsRequestor requestor) {
        Assert.isLegal(requestor != null);
        this.requestor = requestor;
    }
    
    public void build() {
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (IProject project : projects) {
            requestor.accept(project);
        }
    }
    
}
