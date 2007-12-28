package com.yoursway.model.repository;

public interface IBasicModelRegistry {
    
    <T> IBasicModelChangesRequestor addBasicModel(Class<T> rootInterface, T root);
    
}
