package com.yoursway.rails.core.models;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.utils.PathUtils;
import com.yoursway.utils.RailsNamingConventions;

public class RailsModel {
    
    private final IFile file;
    private final String[] pathComponents;
    private final String[] fullClassName;
    private final RailsProject railsProject;
    
    public RailsModel(RailsProject railsProject, IFile file) {
        if (railsProject == null)
            throw new IllegalArgumentException();
        if (file == null)
            throw new IllegalArgumentException();
        
        this.railsProject = railsProject;
        this.file = file;
        
        pathComponents = PathUtils.determinePathComponents(file.getProject().getFolder(
                RailsNamingConventions.APP_MODELS), file);
        fullClassName = RailsNamingConventions.camelize(pathComponents);
    }
    
    public RailsProject getRailsProject() {
        return railsProject;
    }
    
    public IFile getFile() {
        return file;
    }
    
    public String[] getPathComponents() {
        return pathComponents;
    }
    
    public String[] getFullClassName() {
        return fullClassName;
    }
    
    public String getCombinedClassName() {
        return RailsNamingConventions.joinNamespaces(getFullClassName());
    }
    
    public String getTableName() {
        // TODO: how namespaced models are mapped into tables?
        return RailsNamingConventions.tableize(railsProject.getInflector(), getCombinedClassName());
    }
    
}
