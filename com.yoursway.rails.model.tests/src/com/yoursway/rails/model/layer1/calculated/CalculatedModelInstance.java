package com.yoursway.rails.model.layer1.calculated;

import com.yoursway.rails.model.layer1.models.IModelInstance;
import com.yoursway.rails.model.layer1.models.ISnapshot;
import com.yoursway.rails.model.layer1.models.ModelFamily;
import com.yoursway.rails.model.layer1.timeline.Timeline;

public abstract class CalculatedModelInstance<S extends ISnapshot> extends ModelConsumer implements
        IModelInstance {
    
    protected final ModelFamily<S> family;
    private ISnapshot latestSnapshot;
    
    public CalculatedModelInstance(ModelFamily<S> family, Timeline timeline) {
        super(timeline);
        this.family = family;
    }
    
    public ISnapshot latestSnapshotIfExists() {
        return latestSnapshot;
    }
    
    @Override
    protected void pizda() {
        latestSnapshot = createSnapshot();
    }
    
    protected abstract S createSnapshot();
    
}
