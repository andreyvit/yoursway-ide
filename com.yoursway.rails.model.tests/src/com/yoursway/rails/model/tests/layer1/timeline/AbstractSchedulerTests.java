package com.yoursway.rails.model.tests.layer1.timeline;

import java.util.concurrent.Executors;

import junit.framework.Assert;

import com.yoursway.model.repository.Scheduler;
import com.yoursway.model.timeline.Timeline;
import com.yoursway.model.timeline.TimelineBuilder;

public class AbstractSchedulerTests extends Assert {
    
    public AbstractSchedulerTests() {
        super();
    }
    
    protected Scheduler createScheduler() {
        TimelineBuilder timelineBuilder = new TimelineBuilder();
        Timeline timeline = timelineBuilder.build();
        return new Scheduler(timeline, Executors.newSingleThreadExecutor()); // for synch
    }
    
    
}
