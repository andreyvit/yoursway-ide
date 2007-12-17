package com.yoursway.model.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import com.yoursway.model.timeline.PointInTime;
import com.yoursway.model.timeline.Timeline;
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
        BasicModelTracker tracker = new BasicModelTracker(rootHandleInterface, rootHandle, this,
                executorService);
        basicModels.put(rootHandleInterface, tracker);
        return tracker;
    }
    
    public void addConsumer(IConsumer consumer) {
        ConsumerTracker consumerTracker = new ConsumerTracker(consumer, this);
        consumers.add(consumerTracker);
        consumerTracker.call(timeline.now());
    }
    
    public void registerModel(ICalculatedModelUpdater modelUpdater) {
        // TODO
    }
    
    @SuppressWarnings("unchecked")
    public <V extends IModelRoot> V obtainRoot(Class<V> rootInterface) {
        BasicModelTracker tracker = basicModels.get(rootInterface);
        V result = (V) tracker.getRootHandle();
        if (result == null)
            throw new AssertionError("No model provides a root of type " + rootInterface);
        return result;
    }
    
    public void addDependency(ConsumerTracker tracker, IHandle<?> handle) {
        dependencies.put(handle, tracker);
    }
    
    public void handlesChanged(PointInTime moment, BasicModelDelta delta) {
        Set<ConsumerTracker> trackersToUpdate = new HashSet<ConsumerTracker>();
        for (IHandle<?> handle : delta.getChangedHandles())
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
