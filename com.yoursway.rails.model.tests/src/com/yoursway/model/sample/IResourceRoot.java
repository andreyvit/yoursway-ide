package com.yoursway.model.sample;

import com.yoursway.model.repository.IHandle;

public interface IResourceRoot extends IHandle<Integer> {
    
    ICollection<IResourceProject> projects();
    
}
