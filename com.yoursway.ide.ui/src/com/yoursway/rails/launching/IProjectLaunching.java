package com.yoursway.rails.launching;

import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.ruby.RubyInstance;

public interface IProjectLaunching {
    
    RailsProject getRailsProject();
    
    LaunchState getState();
    
    void startDefaultServer();
    
    void stopServer();
    
    public final static class PortNumberNotAvailable extends Exception {
        
        private static final long serialVersionUID = 1L;
        
    }
    
    int getPortNumber() throws PortNumberNotAvailable;
    
    RubyInstance getRubyInstanceToRunTools();
    
}
