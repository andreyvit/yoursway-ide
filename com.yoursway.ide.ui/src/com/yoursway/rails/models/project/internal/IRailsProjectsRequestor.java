/**
 * 
 */
package com.yoursway.rails.models.project.internal;

import org.eclipse.core.resources.IProject;

public interface IRailsProjectsRequestor {
    
    void accept(IProject project);
    
}