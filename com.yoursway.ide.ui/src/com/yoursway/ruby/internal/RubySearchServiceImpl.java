package com.yoursway.ruby.internal;

import com.yoursway.common.TypedListenerList;
import com.yoursway.ruby.RubyInstanceSearchListener;
import com.yoursway.ruby.RubySearchService;

public class RubySearchServiceImpl implements RubySearchService {
    private final RubyInstanceCollection rubyInstanceCollection;
    
    public RubySearchServiceImpl(RubyInstanceCollection rubyInstanceCollection) {
        this.rubyInstanceCollection = rubyInstanceCollection;
    }
    
    private final TypedListenerList<RubyInstanceSearchListener> listeners = new TypedListenerList<RubyInstanceSearchListener>();
    
    public void addInstanceSearchListener(RubyInstanceSearchListener listener) {
        listeners.add(listener);
    }
    
    public void removeInstanceSearchListener(RubyInstanceSearchListener listener) {
        listeners.remove(listener);
    }
    
    public void start() {
        for (RubyInstanceSearchListener listener : listeners)
            listener.searchStarting();
        try {
            search(rubyInstanceCollection);
        } finally {
            for (RubyInstanceSearchListener listener : listeners)
                listener.searchFinished();
        }
    }
    
    private void search(RubyInstanceCollection rubyInstances) {
        
    }
}
