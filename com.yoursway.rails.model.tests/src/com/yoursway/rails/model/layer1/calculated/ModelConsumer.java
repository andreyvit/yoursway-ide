package com.yoursway.rails.model.layer1.calculated;

import java.util.HashSet;
import java.util.Set;

import com.yoursway.model.repository.IHandle;
import com.yoursway.rails.model.layer1.models.ModelFamily;
import com.yoursway.rails.model.layer1.timeline.PointInTime;
import com.yoursway.rails.model.layer1.timeline.Timeline;

public abstract class ModelConsumer {
    
    protected final Timeline timeline;
    
    private PointInTime moment;
    
    private Set<ModelFamily<?>> dependencies = new HashSet<ModelFamily<?>>(); 
    
    public ModelConsumer(Timeline timeline) {
        this.timeline = timeline;
    }
    
    public Set<ModelFamily<?>> pizdecHappened(PointInTime newMoment) {
        if (moment != null && moment.compareTo(newMoment) < 0)
            throw new IllegalArgumentException("PointInTimes passed to ModelConsumer must form a non-decreasing sequence");
        moment = newMoment;
        dependencies.clear();
        pizda();
        return dependencies;
    }
    
    protected abstract void pizda();
    
    protected final <V> V get(ModelFamily<?> family, IHandle<V> handle) {
        dependencies.add(family);
        return null;
    }
    
}
