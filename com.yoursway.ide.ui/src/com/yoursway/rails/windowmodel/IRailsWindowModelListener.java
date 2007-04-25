package com.yoursway.rails.windowmodel;

public interface IRailsWindowModelListener {
    
    void activeProjectChanged(RailsWindowModelProjectChange event);
    
    void activeModeChanged(RailsWindowModelModeChange event);
    
}
