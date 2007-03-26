package com.yoursway.ide.analysis.model;

public class AdviceDelta {
    
    private static final int ADDED = 1;
    
    private static final int REMOVED = 2;
    
    private static final int CHANGED = 3;
    
    private final IAdvice advice;
    
    private final int kind;
    
    public AdviceDelta(IAdvice advice, int kind) {
        this.advice = advice;
        this.kind = kind;
    }
    
    public IAdvice getAdvice() {
        return advice;
    }
    
    /**
     * One of <code>ADDED</code>, <code>REMOVED</code> or
     * <code>CHANGED</code>.
     */
    public int getKind() {
        return kind;
    }
    
}
