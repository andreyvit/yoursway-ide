package com.yoursway.model.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import com.yoursway.rails.model.layer1.timeline.PointInTime;
import com.yoursway.rails.model.layer1.timeline.Timeline;
import com.yoursway.utils.collections.HashSetMultiMap;
import com.yoursway.utils.collections.MultiMap;

public class Scheduler implements IRepository, ConsumerTrackerMaster, BasicModelTrackerMaster {
    
    private final ExecutorService executorService;
    private final Timeline timeline;
    
    private final Collection<ConsumerTracker> consumers = new ArrayList<ConsumerTracker>();
    
    private final Map<Class<?>, BasicModelTracker> basicModels = new HashMap<Class<?>, BasicModelTracker>();
    
    private final MultiMap<IHandle<?>, ConsumerTracker> dependencies = new HashSetMultiMap<IHandle<?>, ConsumerTracker>();
    
    public Scheduler(Timeline timeline, ExecutorService executorService) {
        this.timeline = timeline;
        this.executorService = executorService;
    }
    
    public <T> IBasicModelChangesRequestor addBasicModel(Class<T> rootHandleInterface, T rootHandle) {
        BasicModelTracker tracker = new BasicModelTracker(rootHandleInterface, rootHandle, this);
        basicModels.put(rootHandleInterface, tracker);
        return tracker;
    }
    
    public void addConsumer(IConsumer consumer) {
        ConsumerTracker consumerTracker = new ConsumerTracker(consumer, this);
        consumers.add(consumerTracker);
        consumerTracker.call(timeline.now());
    }
    
    @SuppressWarnings("unchecked")
    public <V> V obtain(Class<V> rootHandleInterface) {
        V result = (V) basicModels.get(rootHandleInterface);
        if (result == null)
            throw new AssertionError("No model provides a root handle of type " + rootHandleInterface);
        return result;
    }
    
    public void addDependency(ConsumerTracker tracker, IHandle<?> handle) {
        dependencies.put(handle, tracker);
    }

    public void handlesChanged(PointInTime moment, Set<IHandle<?>> handles) {
        Set<ConsumerTracker> trackersToUpdate = new HashSet<ConsumerTracker>();
        for (IHandle<?> handle : handles)
            trackersToUpdate.addAll(dependencies.get(handle));
        update(moment, trackersToUpdate);
    }

    private void update(PointInTime moment, Set<ConsumerTracker> trackersToUpdate) {
        for (ConsumerTracker tracker : trackersToUpdate)
            tracker.call(moment);
    }

    public PointInTime createPointInTime() {
        return timeline.advanceThisCrazyWorldToTheNextMomentInTime();
    }
    
}
