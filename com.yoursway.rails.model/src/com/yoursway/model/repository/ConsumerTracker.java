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
    private Resolver previousResolver;
    private final ISnapshotStorage storage;
    
    public ConsumerTracker(IConsumer consumer, ConsumerTrackerMaster master, ExecutorService service,
            ISnapshotStorage storage) {
        this.consumer = consumer;
        this.master = master;
        this.service = service;
        this.storage = storage;
        this.previousResolver = null;
    }
    
    public void call(PointInTime moment, ModelDelta delta) {
        if (previousResolver != null) {
            storage.disposeResolver(previousResolver);
            previousResolver = null;
        }
        final Resolver resolver = new Resolver(moment, master, this, storage, delta);
        service.execute(new Runnable() {
            
            public void run() {
                master.clearDependencies(ConsumerTracker.this);
                consumer.consume(resolver);
                if (resolver.inGodMode())
                    previousResolver = resolver;
                else
                    storage.disposeResolver(resolver);
            }
            
        });
    }
    
    public void dependency(IHandle<?> handle) {
        master.addDependency(this, handle);
    }
    
}
