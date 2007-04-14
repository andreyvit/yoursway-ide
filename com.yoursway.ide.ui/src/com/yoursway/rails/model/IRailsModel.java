package com.yoursway.rails.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.dltk.core.IType;

public interface IRailsModel {
    
    String[] getExpectedClassName();
    
    IFile getCorrespondingFile();
    
    IType getCorrespondingType();
    
}
