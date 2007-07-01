/**
 * 
 */
package com.yoursway.rails.core.controllers.internal;

import org.eclipse.core.resources.IFile;

public interface IRailsControllersRequestor {
    
    void accept(IFile controller);
    
}