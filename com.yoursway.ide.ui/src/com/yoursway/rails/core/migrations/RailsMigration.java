package com.yoursway.rails.core.migrations;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.core.projects.RailsProject;

public class RailsMigration {
    
    private final RailsProject railsProject;
    
    private final IFile file;
    
    private Integer ordinal;
    
    private String expectedClassName;
    
    public RailsMigration(RailsProject railsProject, IFile file) {
        Assert.isNotNull(railsProject);
        Assert.isNotNull(file);
        
        this.railsProject = railsProject;
        this.file = file;
        
        //        String fileName = file.getName();
        //        final Matcher matcher = NAME_PATTERN.matcher(fileName);
        //        if (matcher.find()) {
        //            ordinal = Integer.parseInt(matcher.group(1));
        //            String pureName = matcher.group(2);
        //            expectedClassName = RailsNamingConventions.camelize(pureName);
        //        }
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
