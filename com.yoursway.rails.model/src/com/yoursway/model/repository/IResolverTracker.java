package com.yoursway.model.repository;

import com.yoursway.model.timeline.PointInTime;

public interface IResolverTracker {
    
    void registerResolver(IResolver resolver, PointInTime point);
    
    void disposeResolver(IResolver resolver);
    
}