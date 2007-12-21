/**
 * 
 */
package com.yoursway.model.repository;

import com.yoursway.model.timeline.PointInTime;

public class ConsumerTracker implements DependencyRequestor {
    
    private final IConsumer consumer;
    private final ConsumerTrackerMaster master;
    
    public ConsumerTracker(IConsumer consumer, ConsumerTrackerMaster master) {
        this.consumer = consumer;
        this.master = master;
    }
    
    public void call(SimpleSnapshotStorage storage, PointInTime moment) {
        Resolver resolver = new Resolver(moment, master, this, storage);
        storage.registerResolver(resolver, moment);
        consumer.consume(resolver);
    }
    
    public void dependency(IHandle<?> handle) {
        master.addDependency(this, handle);
    }
    
}
