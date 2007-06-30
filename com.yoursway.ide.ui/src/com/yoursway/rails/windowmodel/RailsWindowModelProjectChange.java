package com.yoursway.rails.windowmodel;

import org.eclipse.ui.IWorkbenchWindow;

import com.yoursway.rails.models.project.RailsProject;

public class RailsWindowModelProjectChange extends RailsWindowModelChange {
    
    private final RailsProject oldMapping;
    private final RailsProject newMapping;
    
    public RailsWindowModelProjectChange(IWorkbenchWindow window, RailsProject oldProject,
            RailsProject newProject) {
        super(window);
        this.oldMapping = oldProject;
        this.newMapping = newProject;
    }
    
    public RailsProject getOldMapping() {
        return oldMapping;
    }
    
    public RailsProject getNewMapping() {
        return newMapping;
    }
    
    @Override
    void sendTo(IRailsWindowModelListener listener) {
        listener.activeProjectChanged(this);
    }
    
}
