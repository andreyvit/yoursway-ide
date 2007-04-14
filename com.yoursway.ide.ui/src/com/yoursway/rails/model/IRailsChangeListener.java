package com.yoursway.rails.model;

import com.yoursway.rails.model.deltas.RailsChangeEvent;

public interface IRailsChangeListener {
    
    void railsModelChanged(RailsChangeEvent event);
    
}
