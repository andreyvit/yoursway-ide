package com.yoursway.model.repository;


public interface ICalculatedModelUpdater {
    
    SnapshotDeltaPair update(IResolver resolver);
    
}
