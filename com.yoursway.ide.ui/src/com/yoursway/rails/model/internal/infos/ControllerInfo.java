package com.yoursway.rails.model.internal.infos;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;

import com.yoursway.utils.PathUtils;
import com.yoursway.utils.RailsNamingConventions;

public class ControllerInfo {
    
    private final IFile file;
    private final String[] pathComponents;
    private final String[] fullClassName;
    private final ProjectInfo projectInfo;
    
    public ControllerInfo(ProjectInfo projectInfo, IFile file) {
        Assert.isLegal(projectInfo != null);
        Assert.isLegal(file != null);
        
        this.projectInfo = projectInfo;
        this.file = file;
        
        pathComponents = PathUtils.determinePathComponents(file.getProject().getFolder(
                RailsNamingConventions.APP_CONTROLLERS), file);
        fullClassName = RailsNamingConventions.camelize(pathComponents);
        if (fullClassName.length == 1 && fullClassName[0].equals("Application"))
            fullClassName[0] = "ApplicationController";
    }
    
    public ProjectInfo getProjectInfo() {
        return projectInfo;
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
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((file == null) ? 0 : file.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ControllerInfo other = (ControllerInfo) obj;
        if (file == null) {
            if (other.file != null)
                return false;
        } else if (!file.equals(other.file))
            return false;
        return true;
    }
    
}
