package com.yoursway.rails.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.dltk.core.IType;

public interface IRailsController extends IProvidesRailsProject {
    
    public enum Caching {
        
        NORMAL,

        CACHED_ONLY
        
    }
    
    IFile getFile();
    
    String[] getPathComponents();
    
    String[] getExpectedClassName();
    
    IType getCorrespondingType(Caching caching);
    
    IRailsControllerActionsCollection getActionsCollection();
    
    IRailsControllerViewsCollection getViewsCollection();
    
}
