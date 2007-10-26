package com.yoursway.model.rails;

import com.yoursway.model.repository.IHandle;

public interface IRailsMigration {
    IHandle<Integer> getOrderNo();
    
    IHandle<String> getName();
}
