/**
 * 
 */
package com.yoursway.model.repository;

import com.yoursway.model.timeline.PointInTime;

public class ConsumerTracker implements DependencyRequestor {
    
    private final IConsumer consumer;
    private PointInTime previousMoment = null;
    private final ConsumerTrackerMaster master;
    
    public ConsumerTracker(IConsumer consumer, ConsumerTrackerMaster master) {
        this.consumer = consumer;
        this.master = master;
    }
    
    public void call(PointInTime moment) {
        Resolver resolver = new Resolver(moment, master, this);
        consumer.consume(resolver);
        this.previousMoment = moment;
    }
    
    public void dependency(IHandle<?> handle) {
        master.addDependency(this, handle);
    }
    
}
