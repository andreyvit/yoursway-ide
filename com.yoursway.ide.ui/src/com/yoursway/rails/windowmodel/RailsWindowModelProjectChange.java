package com.yoursway.rails.windowmodel;

import org.eclipse.ui.IWorkbenchWindow;

import com.yoursway.rails.model.IRailsProject;

public class RailsWindowModelProjectChange extends RailsWindowModelChange {
    
    private final IRailsProject oldMapping;
    private final IRailsProject newMapping;
    
    public RailsWindowModelProjectChange(IWorkbenchWindow window, IRailsProject oldMapping,
            IRailsProject newMapping) {
        super(window);
        this.oldMapping = oldMapping;
        this.newMapping = newMapping;
    }
    
    public IRailsProject getOldMapping() {
        return oldMapping;
    }
    
    public IRailsProject getNewMapping() {
        return newMapping;
    }
    
    @Override
    void sendTo(IRailsWindowModelListener listener) {
        listener.activeProjectChanged(this);
    }
    
}
