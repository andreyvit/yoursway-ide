package com.yoursway.model.resource.internal;

import java.util.Collection;

import org.eclipse.core.resources.IResourceChangeEvent;

import com.yoursway.model.repository.IBasicModelChangesRequestor;
import com.yoursway.model.repository.IBasicModelRegistry;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.resource.IResourceModelRoot;
import com.yoursway.model.resource.IResourceProject;

public class ResourceModel {
    
    private final IResourceModelRoot resourceModelRoot = new IResourceModelRoot() {
        
        public IHandle<Collection<IResourceProject>> projects() {
            return projectsCollectionHandle;
        }
        
    };
    
    private final IHandle<Collection<IResourceProject>> projectsCollectionHandle = new IHandle<Collection<IResourceProject>>() {
        
        @Override
        public String toString() {
            return "resource-projects";
        }
        
    };
    
    private final WorkspaceResourceChangeListener changeListener = new WorkspaceResourceChangeListener() {
        
        public void resourceChanged(IResourceChangeEvent event) {
            // TODO
        }
        
    };
    
    private final IBasicModelChangesRequestor requestor;
    
    public ResourceModel(IBasicModelRegistry registry) {
        requestor = registry.addBasicModel(IResourceModelRoot.class, resourceModelRoot);
    }
    
}
