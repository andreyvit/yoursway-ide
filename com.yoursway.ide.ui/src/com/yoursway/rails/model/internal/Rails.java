package com.yoursway.rails.model.internal;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import com.yoursway.rails.model.IRails;
import com.yoursway.rails.model.IRailsChangeListener;
import com.yoursway.rails.model.IRailsProjectsCollection;
import com.yoursway.utils.TypedListenerList;

public class Rails implements IRails, IResourceChangeListener {
    
    private static Rails INSTANCE = new Rails();
    
    private final RailsProjectsCollection projectsCollection;
    
    private final IWorkspaceRoot workspaceRoot;
    
    private final TypedListenerList<IRailsChangeListener> listeners = new TypedListenerList<IRailsChangeListener>() {
        
        @Override
        protected IRailsChangeListener[] makeArray(int size) {
            return new IRailsChangeListener[size];
        }
        
    };
    
    public static Rails instance() {
        return INSTANCE;
    }
    
    public Rails() {
        install();
        workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        projectsCollection = new RailsProjectsCollection(workspaceRoot);
    }
    
    public void refresh() {
        RailsDeltaBuilder deltaBuilder = new RailsDeltaBuilder(this);
        deltaBuilder.somethingChanged();
        projectsCollection.refresh();
        deltaBuilder.fire(listeners.getListeners());
    }
    
    private void install() {
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }
    
    public void resourceChanged(IResourceChangeEvent event) {
        if (event.getType() != IResourceChangeEvent.POST_CHANGE)
            return;
        IResourceDelta delta = event.getDelta();
        RailsDeltaBuilder deltaBuilder = new RailsDeltaBuilder(this);
        reconcile(delta, deltaBuilder);
        deltaBuilder.fire(listeners.getListeners());
    }
    
    private void reconcile(IResourceDelta parentDelta, RailsDeltaBuilder deltaBuilder) {
        projectsCollection.reconcile(deltaBuilder, parentDelta);
    }
    
    public IRailsProjectsCollection getProjectsCollection() {
        return projectsCollection;
    }
    
    public void addChangeListener(IRailsChangeListener listener) {
        listeners.add(listener);
    }
    
    public void removeChangeListener(IRailsChangeListener listener) {
        listeners.remove(listener);
    }
    
}
