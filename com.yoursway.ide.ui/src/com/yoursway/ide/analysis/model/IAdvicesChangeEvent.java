package com.yoursway.ide.analysis.model;

import java.util.Iterator;

public interface IAdvicesChangeEvent {
    
    /**
     * @return A complete list of advice changes, or <code>null</code> if the
     *         whole list of advices should be considered changed.
     */
    Iterator<AdviceDelta> iterator();
    
}