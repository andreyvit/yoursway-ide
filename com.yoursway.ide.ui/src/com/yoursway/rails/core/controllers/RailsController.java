package com.yoursway.rails.core.controllers;

import org.eclipse.core.resources.IFile;

import com.yoursway.common.SegmentedName;
import com.yoursway.rails.commons.RailsNamingConventions;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.utils.PathUtils;

public class RailsController {
    
    private final IFile file;
    private final SegmentedName pathComponents;
    private final SegmentedName fullClassName;
    private final RailsProject railsProject;
    
    public RailsController(RailsProject railsProject, IFile file) {
        if (railsProject == null)
            throw new IllegalArgumentException();
        if (file == null)
            throw new IllegalArgumentException();
        
        this.railsProject = railsProject;
        this.file = file;
        
        pathComponents = new SegmentedName(PathUtils.determinePathComponents(file.getProject().getFolder(
                RailsNamingConventions.APP_CONTROLLERS), file));
        fullClassName = calculateClassName(pathComponents);
    }
    
    private static SegmentedName calculateClassName(final SegmentedName pathComponents) {
        SegmentedName fullClassName = RailsNamingConventions.camelize(pathComponents);
        if (fullClassName.equalsSingleSegment("Application"))
            fullClassName = fullClassName.replaceTrailingSegment("ApplicationController");
        return fullClassName;
    }
    
    public RailsProject getRailsProject() {
        return railsProject;
    }
    
    public IFile getFile() {
        return file;
    }
    
    public SegmentedName getPathComponents() {
        return pathComponents;
    }
    
    public SegmentedName getFullClassName() {
        return fullClassName;
    }
    
    public String getDisplayName() {
        return RailsNamingConventions.joinNamespaces(getFullClassName());
    }
    
    public SegmentedName getNamespace() {
        return getFullClassName().removeTrailingSegment();
    }
    
}
