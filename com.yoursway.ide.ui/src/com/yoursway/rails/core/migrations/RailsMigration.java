package com.yoursway.rails.core.migrations;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.core.migrations.internal.RailsMigrationInfo;
import com.yoursway.rails.core.projects.RailsProject;

public class RailsMigration {
    
    private final RailsProject railsProject;
    
    private final IFile file;
    
    private final Integer ordinal;
    
    private final String expectedClassName;
    
    public RailsMigration(RailsProject railsProject, RailsMigrationInfo data) {
        Assert.isNotNull(railsProject);
        Assert.isNotNull(data);
        
        this.railsProject = railsProject;
        file = data.getFile();
        ordinal = data.getOrdinal();
        expectedClassName = data.getExpectedClassName();
    }
    
    public RailsProject getRailsProject() {
        return railsProject;
    }
    
    public IFile getFile() {
        return file;
    }
    
    public Integer getOrdinal() {
        return ordinal;
    }
    
    public String getExpectedClassName() {
        return expectedClassName;
    }
    
}
