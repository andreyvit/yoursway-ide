package com.yoursway.rails.model;

import com.yoursway.rails.model.internal.Rails;

public class RailsCore {
    
    public static IRails instance() {
        return Rails.instance();
    }
    
}
