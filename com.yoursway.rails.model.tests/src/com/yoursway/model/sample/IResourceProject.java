package com.yoursway.model.sample;

public interface IResourceProject {
    
    String getName();
    
    ICollection<IResourceFile> files();
    
}
