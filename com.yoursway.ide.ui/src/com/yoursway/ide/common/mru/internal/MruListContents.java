package com.yoursway.ide.common.mru.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MruListContents<E> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private List<E> entries;
    
    public MruListContents() {
        entries = new ArrayList<E>();
    }
    
    public List<E> getEntries() {
        return entries;
    }
    
    public void setEntries(List<E> entries) {
        this.entries = entries;
    }
    
}
