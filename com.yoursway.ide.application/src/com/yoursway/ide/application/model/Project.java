package com.yoursway.ide.application.model;

import java.io.File;

public class Project {
    
    private final ProjectOwner owner;
    private final File location;

    public Project(ProjectOwner owner, File location) {
        this.owner = owner;
        this.location = location;
    }
    
}
