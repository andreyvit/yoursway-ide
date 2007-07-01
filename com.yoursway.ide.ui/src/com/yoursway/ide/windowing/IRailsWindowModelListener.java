package com.yoursway.ide.windowing;

public interface IRailsWindowModelListener {
    
    void activeProjectChanged(RailsWindowModelProjectChange event);
    
    void activeModeChanged(RailsWindowModelModeChange event);
    
}
