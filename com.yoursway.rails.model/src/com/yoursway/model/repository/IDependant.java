package com.yoursway.model.repository;

import com.yoursway.model.timeline.PointInTime;

public interface IDependant {
    
    void call(PointInTime moment, ModelDelta delta);
    
}
