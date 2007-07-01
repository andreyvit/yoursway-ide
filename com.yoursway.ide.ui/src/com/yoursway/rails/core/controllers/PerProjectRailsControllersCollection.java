package com.yoursway.rails.core.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.core.internal.support.AbstractModel;
import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.core.controllers.internal.BroadcastingChangeVisitor;
import com.yoursway.rails.core.controllers.internal.RailsControllersIterator;
import com.yoursway.rails.core.controllers.internal.Requestor;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.utils.RailsNamingConventions;

public class PerProjectRailsControllersCollection extends AbstractModel<IControllersListener> {
    
    private Map<IFile, RailsController> items = new HashMap<IFile, RailsController>();
    private final RailsProject railsProject;
    
    public PerProjectRailsControllersCollection(RailsProject railsProject) {
        this.railsProject = railsProject;
        rebuild();
    }
    
    public RailsProject getRailsProject() {
        return railsProject;
    }
    
    public Collection<RailsController> getAll() {
        return items.values();
    }
    
    public RailsController get(IFile project) {
        return items.get(project);
    }
    
    public void rebuild() {
        Requestor updater = new Requestor(railsProject, items);
        new RailsControllersIterator(railsProject, updater).build();
        items = updater.getNewItems();
        updater.visitChanges(new BroadcastingChangeVisitor(getListeners()));
    }
    
    @Override
    protected IControllersListener[] makeListenersArray(int size) {
        return new IControllersListener[size];
    }
    
    public void reconcile(IResourceDelta projectRD) {
        rebuild();
        IResourceDelta controllersRD = projectRD.findMember(RailsNamingConventions.APP_CONTROLLERS_PATH);
        try {
            controllersRD.accept(new IResourceDeltaVisitor() {
                
                public boolean visit(IResourceDelta delta) throws CoreException {
                    if (delta.getResource().getType() == IResource.FILE) {
                        IFile file = (IFile) delta.getResource();
                        RailsController railsController = items.get(file);
                        if (railsController != null)
                            for (IControllersListener listener : getListeners())
                                listener.reconcile(railsController);
                    }
                    return true;
                }
                
            });
        } catch (CoreException e) {
            Activator.unexpectedError(e);
        }
    }
}
