package com.yoursway.ide.application.view.impl;

public class ViewArea {
    
    private final String debuggingName;
    private final boolean multipleAllowed;

    public ViewArea(String debuggingName, boolean multipleAllowed) {
        if (debuggingName == null)
            throw new NullPointerException("debuggingName is null");
        this.debuggingName = debuggingName;
        this.multipleAllowed = multipleAllowed;
    }
    
    public boolean areMultipleContributionsAllowed() {
        return multipleAllowed;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + debuggingName + ")";
    }
    
}
