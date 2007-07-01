/**
 * 
 */
package com.yoursway.rails.core.projects.internal;

import org.eclipse.core.resources.IProject;

public interface IRailsProjectsRequestor {
    
    void accept(IProject project);
    
}