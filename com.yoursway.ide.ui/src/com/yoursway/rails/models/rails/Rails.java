package com.yoursway.rails.models.rails;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;

import com.yoursway.rails.models.AbstractModel;

public class Rails extends AbstractModel<IRailsListener> implements IResourceChangeListener {
    
    private static final Rails INSTANCE = new Rails();
    
    public static Rails getInstance() {
        return INSTANCE;
    }
    
    public Rails() {
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }
    
    public void resourceChanged(IResourceChangeEvent event) {
        if (event.getType() != IResourceChangeEvent.POST_CHANGE)
            return;
        IResourceDelta delta = event.getDelta();
        for (IRailsListener listener : getListeners())
            listener.reconcile(delta);
    }
    
    @Override
    protected IRailsListener[] makeListenersArray(int size) {
        return new IRailsListener[size];
    }
    
}
