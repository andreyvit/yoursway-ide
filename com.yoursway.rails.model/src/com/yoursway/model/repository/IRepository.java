package com.yoursway.model.repository;

public interface IRepository {
    
    <T> IBasicModelChangesRequestor addBasicModel(Class<T> rootInterface, T root);
    
    void addConsumer(IConsumer consumer);
    
}
