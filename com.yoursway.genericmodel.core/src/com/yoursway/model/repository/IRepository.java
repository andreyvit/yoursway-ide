package com.yoursway.model.repository;

public interface IRepository extends IBasicModelRegistry {
    
    void addConsumer(IConsumer consumer);

    IResolver addBackgroundConsumer(IConsumer consumer);

    <T> void registerModel(Class<T> rootHandleInterface, T rootHandle,
            ICalculatedModelUpdater modelUpdater);

}
