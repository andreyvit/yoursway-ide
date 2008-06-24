package com.yoursway.ide.application.model;

import java.io.File;

import com.yoursway.ide.application.model.projects.types.ProjectType;

public class Project {
    
    private final ProjectOwner owner;
    private final File location;
    private final ProjectType type;

    public Project(ProjectOwner owner, ProjectType type, File location) {
        if (owner == null)
            throw new NullPointerException("owner is null");
        if (type == null)
            throw new NullPointerException("type is null");
        if (location == null)
            throw new NullPointerException("location is null");
        
        this.owner = owner;
        this.type = type;
        this.location = location;
    }
    
    public File getLocation() {
        return location;
    }
    
    public ProjectType getType() {
        return type;
    }
    
}
