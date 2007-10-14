/**
 * 
 */
package com.yoursway.rails.model.tests.layer1.calculated;

import com.yoursway.rails.model.layer1.models.ModelFamily;
import com.yoursway.rails.model.layer1.timeline.PointInTime;

public final class FooFamily extends ModelFamily<FooSnapshot> {
    @Override
    public FooSnapshot buildSnapshot(PointInTime time, FooSnapshot previousSnapshot) {
        return null;
    }
}