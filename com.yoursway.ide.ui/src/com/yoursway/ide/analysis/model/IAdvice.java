package com.yoursway.ide.analysis.model;

import org.eclipse.swt.graphics.GC;

public interface IAdvice {
    
    int getPriority();
    
    int getLineNumber();
    
    void paint(GC gc, int x, int y);
    
}
