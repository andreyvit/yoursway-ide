package com.yoursway.rails.model.tests;

import java.util.ArrayList;
import java.util.Collection;

public class ModelFamily {
    
    private Collection<IModelListener> listeners = new ArrayList<IModelListener>();
    
    public void subscribe(IModelListener listener) {
    }

    public void unsubscribe(IModelListener listener) {
    }
    
}
