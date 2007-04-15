package com.yoursway.rails.windowmodel;

import org.eclipse.ui.IWorkbenchWindow;

import com.yoursway.rails.model.IRailsProject;

public class RailsWindowModelChange {
    
    private final IWorkbenchWindow window;
    private final IRailsProject oldMapping;
    private final IRailsProject newMapping;
    
    public RailsWindowModelChange(IWorkbenchWindow window, IRailsProject oldMapping, IRailsProject newMapping) {
        this.window = window;
        this.oldMapping = oldMapping;
        this.newMapping = newMapping;
    }
    
    public IWorkbenchWindow getWindow() {
        return window;
    }
    
    public IRailsProject getOldMapping() {
        return oldMapping;
    }
    
    public IRailsProject getNewMapping() {
        return newMapping;
    }
    
}
