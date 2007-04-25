package com.yoursway.rails.model;

import org.eclipse.core.resources.IFolder;

public interface IRailsControllersFolder extends IRailsElement, IProvidesRailsProject {
    
    IFolder getCorrespondingFolder();
    
    IRailsControllerSubfoldersCollection getSubfoldersCollection();
    
    IRailsFolderControllersCollection getControllersCollection();
    
    IRailsControllersManager getControllersManager();
    
    IRailsControllersFolder getParentFolder();
    
}
