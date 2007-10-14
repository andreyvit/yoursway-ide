package com.yoursway.rails.model.tests.layer1.calculated;

import com.yoursway.model.repository.IHandle;
import com.yoursway.rails.model.layer1.calculated.CalculatedModelInstance;
import com.yoursway.rails.model.layer1.models.ModelFamily;
import com.yoursway.rails.model.layer1.timeline.Timeline;

public class FooModelInstance extends CalculatedModelInstance<FooSnapshot> {

    public FooModelInstance(ModelFamily<FooSnapshot> family, Timeline timeline) {
        super(family, timeline);
    }

    @Override
    protected FooSnapshot createSnapshot() {
        return new FooSnapshot();
    }

    public IHandle<?> rootHandle() {
        return null;
    }


}
