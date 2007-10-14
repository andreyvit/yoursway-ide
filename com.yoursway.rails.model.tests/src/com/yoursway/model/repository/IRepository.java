package com.yoursway.model.repository;

public interface IRepository {
    
    <T> IBasicModelChangesRequestor addBasicModel(Class<T> rootHandleInterface, T rootHandle);
    
    void addConsumer(IConsumer consumer);
    
}
