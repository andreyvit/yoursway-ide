/**
 * 
 */
package com.yoursway.model.repository;

import java.util.concurrent.ExecutorService;

import com.yoursway.model.timeline.PointInTime;

public class ConsumerTracker implements DependencyRequestor, IDependant {
    
    private final IConsumer consumer;
    private final ConsumerTrackerMaster master;
    private final ExecutorService service;
    
    public ConsumerTracker(IConsumer consumer, ConsumerTrackerMaster master, ExecutorService service) {
        this.consumer = consumer;
        this.master = master;
        this.service = service;
    }
    
    public void call(ISnapshotStorage storage, PointInTime moment, ModelDelta delta) {
        final Resolver resolver = new Resolver(moment, master, this, storage, delta);
        service.execute(new Runnable() {
            
            public void run() {
                master.clearDependencies(ConsumerTracker.this);
                consumer.consume(resolver);
            }
            
        });
    }
    
    public void dependency(IHandle<?> handle) {
        master.addDependency(this, handle);
    }
    
}
