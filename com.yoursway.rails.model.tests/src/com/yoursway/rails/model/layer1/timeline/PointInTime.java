package com.yoursway.rails.model.layer1.timeline;

public final class PointInTime implements Comparable<PointInTime> {
    
    private final int moment;

    public PointInTime(int moment) {
        this.moment = moment;
    }

    public int compareTo(PointInTime o) {
        return moment - o.moment;
    }
    
}
