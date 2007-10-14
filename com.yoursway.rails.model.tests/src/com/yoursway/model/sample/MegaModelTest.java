package com.yoursway.model.sample;

import java.util.concurrent.Executors;

import org.junit.Test;

import com.yoursway.model.repository.Scheduler;
import com.yoursway.rails.model.layer1.timeline.Timeline;
import com.yoursway.rails.model.layer1.timeline.TimelineBuilder;

public class MegaModelTest {
    
    @Test
    public void fuckItAllHard() {
        TimelineBuilder timelineBuilder = new TimelineBuilder();
        Timeline timeline = timelineBuilder.build();
        Scheduler scheduler = new Scheduler(timeline, Executors.newCachedThreadPool());
        FakeResourceRoot rr = new FakeResourceRoot(scheduler);
        scheduler.addConsumer(new MegaModelPrinter());
        rr.simulateChange();
    }
    
}
