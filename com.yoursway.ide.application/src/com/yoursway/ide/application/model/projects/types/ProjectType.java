package com.yoursway.ide.application.model.projects.types;

import java.io.File;

public abstract class ProjectType {
    
    public abstract String generateNewProjectName(File location);
    
}
