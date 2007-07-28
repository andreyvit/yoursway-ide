/**
 * 
 */
package com.yoursway.rails.core.projects.internal;

import com.yoursway.rails.core.internal.support.ComparingUpdater;
import com.yoursway.rails.core.projects.IProjectsListener;
import com.yoursway.rails.core.projects.RailsProject;

public final class BroadcastingChangeVisitor implements ComparingUpdater.IVisitor<RailsProject> {
    private final Iterable<IProjectsListener> listeners;
    
    public BroadcastingChangeVisitor(Iterable<IProjectsListener> listeners) {
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