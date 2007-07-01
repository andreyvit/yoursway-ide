package com.yoursway.ide.windowing;

import org.eclipse.ui.IWorkbenchWindow;

public abstract class RailsWindowModelChange {
    
    private final IWorkbenchWindow window;
    
    public RailsWindowModelChange(IWorkbenchWindow window) {
        this.window = window;
    }
    
    public IWorkbenchWindow getWindow() {
        return window;
    }
    
    abstract void sendTo(IRailsWindowModelListener listener);
    
}
