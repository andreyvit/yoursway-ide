package com.yoursway.genericmodel.ui;

import java.util.Collection;

import com.yoursway.model.repository.IHandle;

public interface INodeElement {
    
    INodeElement getParent();
    
    IHandle<Visualizer> visualizer();
    
    IHandle<Collection<INodeElement>> children();
    
}

