/**
 * 
 */
package com.yoursway.rails.core.fixtures.internal;

import org.eclipse.core.resources.IFile;

public interface IRailsFixturesRequestor {
    
    void accept(IFile controller);
    
}