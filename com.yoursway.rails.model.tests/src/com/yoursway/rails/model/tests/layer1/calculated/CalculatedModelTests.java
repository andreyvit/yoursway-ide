package com.yoursway.rails.model.tests.layer1.calculated;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.yoursway.rails.model.layer1.models.ModelFamily;
import com.yoursway.rails.model.layer1.timeline.PointInTime;
import com.yoursway.rails.model.layer1.timeline.Timeline;
import com.yoursway.rails.model.layer1.timeline.TimelineBuilder;

public class CalculatedModelTests {
    
    private Timeline timeline;
    private ModelFamily<FooSnapshot> fooFamily;
    
    @Before
    public void setUp() {
        TimelineBuilder timelineBuilder = new TimelineBuilder();
        timeline = timelineBuilder.build();
        
        fooFamily = new FooFamily();
    }
    
    @Test
    public void testDumbSnapshotCreation() {
        PointInTime now = timeline.now();
        FooModelInstance fooInstance = new FooModelInstance(fooFamily, timeline);
        fooInstance.pizdecHappened(now);
        Assert.assertNotNull(fooInstance.latestSnapshotIfExists());
    }
    
    @Test
    public void dumb() {
        PointInTime now = timeline.now();
        FooModelInstance fooInstance = new FooModelInstance(fooFamily, timeline);
        fooInstance.pizdecHappened(now);
        Assert.assertNotNull(fooInstance.latestSnapshotIfExists());
    }
    
}
