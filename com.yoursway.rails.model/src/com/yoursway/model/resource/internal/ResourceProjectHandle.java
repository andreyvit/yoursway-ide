package com.yoursway.model.resource.internal;

import org.eclipse.core.resources.IProject;

import com.yoursway.model.repository.IModelElement;
import com.yoursway.model.resource.IResourceProject;

public class ResourceProjectHandle extends ResourceHandle<IResourceProject> {
    
    private final IProject eclipseProject;
    
    public ResourceProjectHandle(IProject eclipseProject) {
        this.eclipseProject = eclipseProject;
    }
    
    @Override
    public String toString() {
        return "projects/" + eclipseProject.getName();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eclipseProject == null) ? 0 : eclipseProject.hashCode());
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
        final ResourceProjectHandle other = (ResourceProjectHandle) obj;
        if (eclipseProject == null) {
            if (other.eclipseProject != null)
                return false;
        } else if (!eclipseProject.equals(other.eclipseProject))
            return false;
        return true;
    }
    
    public IModelElement getModelElement() {
        return null;
    }
    
}
