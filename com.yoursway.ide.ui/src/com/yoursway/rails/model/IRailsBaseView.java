package com.yoursway.rails.model;

import org.eclipse.core.resources.IFile;

public interface IRailsBaseView extends IRailsElement, IProvidesRailsProject {
    
    public enum Format {
        
        UNKNOWN,

        RHTML,

        RXML,

        RJS,

        EJS,

        ROTFLOL,
        
    }
    
    String getName();
    
    Format getFormat();
    
    IFile getCorrespondingFile();
    
    IRailsController getController();
    
}
