package com.yoursway.rails.model.tests;

import java.util.Set;

public class PointInTime {
    
    private final Set<ISnapshot> basicSnapshots;

    public PointInTime(Set<ISnapshot> basicSnapshots) {
        this.basicSnapshots = basicSnapshots;
    }
    
}
