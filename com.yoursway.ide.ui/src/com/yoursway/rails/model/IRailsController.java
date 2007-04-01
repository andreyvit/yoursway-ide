package com.yoursway.rails.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.dltk.core.IType;

public interface IRailsController {
    
    IFile getFile();
    
    IType getType();
    
}
