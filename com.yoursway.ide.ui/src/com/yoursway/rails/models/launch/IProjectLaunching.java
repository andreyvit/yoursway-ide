package com.yoursway.rails.models.launch;

import com.yoursway.rails.model.IRailsProject;

public interface IProjectLaunching {
    
    IRailsProject getRailsProject();
    
    LaunchState getState();
    
    void startDefaultServer();
    
    void stopServer();
    
    public final static class PortNumberNotAvailable extends Exception {
        
        private static final long serialVersionUID = 1L;
        
    }
    
    int getPortNumber() throws PortNumberNotAvailable;
    
}
