package com.yoursway.model.repository;


public interface IModelRootProvider {
    
    <V extends IModelRoot> V obtainRoot(Class<V> rootHandleInterface);
    
}