package com.yoursway.rails.model.tests.layer1.timeline;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.yoursway.rails.model.layer1.timeline.PointInTime;
import com.yoursway.rails.model.layer1.timeline.Timeline;
import com.yoursway.rails.model.layer1.timeline.TimelineBuilder;

public class TimelineTests {
    
    private Timeline timeline;

    @Before
    public void setup() {
        TimelineBuilder timelineBuilder = new TimelineBuilder();
        timeline = timelineBuilder.build();
    }
    
    @Test
    public void hmm() {
        PointInTime a = timeline.now();
        PointInTime b = timeline.advanceThisCrazyWorldToTheNextMomentInTime();
        Assert.assertEquals(-1, a.compareTo(b));
    }
    
}
