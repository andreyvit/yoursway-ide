package com.yoursway.model.resource.internal;

import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceProject;

public abstract class AbstractProjectHandle<T> implements IHandle<T> {
    
    private final IResourceProject project;
    
    public AbstractProjectHandle(IResourceProject project) {
        this.project = project;
    }
    
    public IResourceProject getProject() {
        return project;
    }
    
    public IResourceProject getModelElement() {
        return project;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((project == null) ? 0 : project.hashCode());
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
        AbstractProjectHandle<?> other = (AbstractProjectHandle<?>) obj;
        if (project == null) {
            if (other.project != null)
                return false;
        } else if (!project.equals(other.project))
            return false;
        return true;
    }
    
}
