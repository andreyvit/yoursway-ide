/**
 * 
 */
package com.yoursway.rails.models.project.internal;

import com.yoursway.rails.models.ComparingUpdater;
import com.yoursway.rails.models.project.IProjectsListener;
import com.yoursway.rails.models.project.RailsProject;

public final class BroadcastingChangeVisitor implements ComparingUpdater.IVisitor<RailsProject> {
    private final IProjectsListener[] listeners;
    
    public BroadcastingChangeVisitor(IProjectsListener[] listeners) {
        this.listeners = listeners;
    }
    
    public void visitAdded(RailsProject value) {
        for (IProjectsListener listener : listeners)
            listener.projectAdded(value);
    }
    
    public void visitRemoved(RailsProject value) {
        for (IProjectsListener listener : listeners)
            listener.projectRemoved(value);
    }
}