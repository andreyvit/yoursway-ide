package com.yoursway.rails.models.core.internal.infos;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.models.project.RailsProject;

public interface IControllerInfoRequestor {
    
    void consumeInfo(RailsProject parentProject, IFile file);
    
}
