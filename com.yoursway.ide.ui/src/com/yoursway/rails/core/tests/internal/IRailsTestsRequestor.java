/**
 * 
 */
package com.yoursway.rails.core.tests.internal;

import org.eclipse.core.resources.IFile;

public interface IRailsTestsRequestor {
    
    void accept(IFile controller);
    
}