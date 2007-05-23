package com.yoursway.rails.models.controller;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.models.project.RailsProject;
import com.yoursway.utils.PathUtils;
import com.yoursway.utils.RailsNamingConventions;

public class RailsController {
    
    private final IFile file;
    private final String[] pathComponents;
    private final String[] fullClassName;
    private final RailsProject railsProject;
    
    public RailsController(RailsProject railsProject, IFile file) {
        if (railsProject == null)
            throw new IllegalArgumentException();
        if (file == null)
            throw new IllegalArgumentException();
        
        this.railsProject = railsProject;
        this.file = file;
        
        pathComponents = PathUtils.determinePathComponents(file.getProject().getFolder(
                RailsNamingConventions.APP_CONTROLLERS), file);
        fullClassName = RailsNamingConventions.camelize(pathComponents);
        if (fullClassName.length == 1 && fullClassName[0].equals("Application"))
            fullClassName[0] = "ApplicationController";
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
    
}
