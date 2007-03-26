package com.yoursway.ide.analysis.model;

import java.util.Iterator;

public class AdvicesChangeEvent implements IAdvicesChangeEvent {
    
    private final AdviceDelta[] deltas;
    
    public AdvicesChangeEvent(AdviceDelta[] deltas) {
        this.deltas = deltas;
    }
    
    public Iterator<AdviceDelta> iterator() {
        return new Iterator<AdviceDelta>() {
            
            int index = 0;
            
            public boolean hasNext() {
                return index < deltas.length;
            }
            
            public AdviceDelta next() {
                return deltas[index++];
            }
            
            public void remove() {
                throw new UnsupportedOperationException();
            }
            
        };
    }
    
}
