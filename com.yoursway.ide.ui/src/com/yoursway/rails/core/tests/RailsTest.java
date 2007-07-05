package com.yoursway.rails.core.tests;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.core.projects.RailsProject;

public class RailsTest {
    
    private final RailsProject railsProject;
    private final IFile file;
    
    public RailsTest(RailsProject railsProject, IFile file) {
        if (railsProject == null)
            throw new IllegalArgumentException();
        if (file == null)
            throw new IllegalArgumentException();
        
        this.railsProject = railsProject;
        this.file = file;
    }
    
    public RailsProject getRailsProject() {
        return railsProject;
    }
    
    public IFile getFile() {
        return file;
    }
    
}
