package com.yoursway.model.repository;

public interface IRootHandleProvider {
    
    <V> V obtain(Class<V> rootHandleInterface);
    
}