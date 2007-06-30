/**
 * 
 */
package com.yoursway.rails.models.controller.internal;

import org.eclipse.core.resources.IFile;

public interface IRailsControllersRequestor {
    
    void accept(IFile controller);
    
}