/**
 * 
 */
package com.yoursway.rails.core.models.internal;

import org.eclipse.core.resources.IFile;

public interface IRailsModelsRequestor {
    
    void accept(IFile controller);
    
}