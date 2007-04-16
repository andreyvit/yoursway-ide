package com.yoursway.rails.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.dltk.core.IType;

public interface IRailsModel extends IRailsElement, IProvidesRailsProject {
    
    String[] getPathComponents();
    
    String[] getExpectedClassName();
    
    IFile getCorrespondingFile();
    
    IType getCorrespondingType(Caching caching);
    
    String getName();
    
    String getTableName();
    
}
