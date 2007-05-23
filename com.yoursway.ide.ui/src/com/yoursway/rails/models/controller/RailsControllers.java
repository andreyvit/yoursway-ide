package com.yoursway.rails.models.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;

import com.yoursway.rails.models.AbstractModel;
import com.yoursway.rails.models.rails.IRailsListener;

public class RailsControllers extends AbstractModel<IControllersListener> implements IRailsListener {
    
    private final Map<IFile, RailsController> items = new HashMap<IFile, RailsController>();
    
    public Collection<RailsController> getAll() {
        return items.values();
    }
    
    public RailsController get(IFile project) {
        return items.get(project);
    }
    
    public void rebuild() {
        //        Requestor updater = new Requestor(items);
        //        new RailsProjectsIterator(updater).build();
        //        items = updater.getNewItems();
        //        updater.visitChanges(new BroadcastingChangeVisitor(getListeners()));
    }
    
    @Override
    protected IControllersListener[] makeListenersArray(int size) {
        return new IControllersListener[size];
    }
    
    public void reconcile(IResourceDelta workspaceRD) {
        rebuild();
        IResourceDelta[] children = workspaceRD.getAffectedChildren(IResourceDelta.CHANGED);
        for (IResourceDelta projectResourceDelta : children) {
            IProject project = (IProject) projectResourceDelta.getResource();
            RailsController railsProject = items.get(project);
            if (railsProject == null)
                continue; // not a Rails project
                //            for (IControllersListener listener : getListeners())
                //                listener.reconcile(railsProject, projectResourceDelta);
        }
    }
}
