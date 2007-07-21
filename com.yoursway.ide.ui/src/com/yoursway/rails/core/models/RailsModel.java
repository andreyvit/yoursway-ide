package com.yoursway.rails.core.models;

import org.eclipse.core.resources.IFile;

import com.yoursway.common.SegmentedName;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.utils.PathUtils;
import com.yoursway.utils.RailsNamingConventions;

public class RailsModel {
    
    private final IFile file;
    private final SegmentedName pathComponents;
    private final SegmentedName fullClassName;
    private final RailsProject railsProject;
    
    public RailsModel(RailsProject railsProject, IFile file) {
        if (railsProject == null)
            throw new IllegalArgumentException();
        if (file == null)
            throw new IllegalArgumentException();
        
        this.railsProject = railsProject;
        this.file = file;
        
        pathComponents = new SegmentedName(PathUtils.determinePathComponents(file.getProject().getFolder(
                RailsNamingConventions.APP_MODELS), file));
        fullClassName = RailsNamingConventions.camelize(pathComponents);
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
    
    public String getCombinedClassName() {
        return RailsNamingConventions.joinNamespaces(getFullClassName());
    }
    
    public String getTableName() {
        // TODO: how namespaced models are mapped into tables?
        return RailsNamingConventions.tableize(railsProject.getInflector(), getCombinedClassName());
    }
    
    public SegmentedName getNamespace() {
        return fullClassName.removeTrailingSegment();
    }
    
}
