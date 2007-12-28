package com.yoursway.model.repository;

public interface IPerModelResolver {
    
    <V, H extends IHandle<V>> V get(IHandle<H> handle) throws NoSuchHandleException;
}
