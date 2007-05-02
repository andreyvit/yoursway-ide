package com.yoursway.rails.model.internal.infos;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

public class ProjectInfoBuilder {
    
    public Collection<ProjectInfo> build() {
        final ArrayList<ProjectInfo> result = new ArrayList<ProjectInfo>();
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (IProject project : projects) {
            result.add(build(project));
        }
        return result;
        
    }
    
    private ProjectInfo build(IProject project) {
        return new ProjectInfo(project);
    }
    
}
