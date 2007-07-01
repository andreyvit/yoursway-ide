package com.yoursway.rails.core;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;

import com.yoursway.rails.core.internal.support.AbstractModel;

public class RailsCore extends AbstractModel<IRailsCoreListener> implements IResourceChangeListener {
    
    private static final RailsCore INSTANCE = new RailsCore();
    
    public static RailsCore getInstance() {
        return INSTANCE;
    }
    
    public RailsCore() {
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }
    
    public void resourceChanged(IResourceChangeEvent event) {
        if (event.getType() != IResourceChangeEvent.POST_CHANGE)
            return;
        IResourceDelta delta = event.getDelta();
        for (IRailsCoreListener listener : getListeners())
            listener.reconcile(delta);
    }
    
    @Override
    protected IRailsCoreListener[] makeListenersArray(int size) {
        return new IRailsCoreListener[size];
    }
    
}
