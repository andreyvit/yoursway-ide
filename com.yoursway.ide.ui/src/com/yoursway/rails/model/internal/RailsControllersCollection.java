package com.yoursway.rails.model.internal;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.model.IRailsController;
import com.yoursway.rails.model.IRailsControllersFolder;
import com.yoursway.rails.model.IRailsControllersManager;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.rails.model.deltas.controllers.ControllersFolderAddedDelta;
import com.yoursway.rails.model.deltas.controllers.ControllersFolderDelta;
import com.yoursway.rails.model.deltas.controllers.ControllersFolderRemovedDelta;
import com.yoursway.rails.model.internal.deltas.RailsProjectChangedDeltaBuilder;
import com.yoursway.utils.RailsNamingConventions;

public class RailsControllersCollection extends RailsElement implements IRailsControllersManager {
    
    private final IRailsProject railsProject;
    private RailsControllersFolder rootFolder;
    private boolean opened = false;
    private final IFolder root;
    
    public RailsControllersCollection(IRailsProject railsProject) {
        this.railsProject = railsProject;
        root = railsProject.getProject().getFolder(RailsNamingConventions.APP_CONTROLLERS);
    }
    
    public void refresh() {
        close();
        open();
    }
    
    public void reconcile(RailsProjectChangedDeltaBuilder db, IResourceDelta projectDelta) {
        RailsControllersFolder oldRootFolder = rootFolder;
        
        if (!opened)
            open();
        else if ((rootFolder != null) != root.exists())
            refresh();
        
        IResourceDelta appDelta = projectDelta.findMember(RailsNamingConventions.APP_PATH);
        IResourceDelta controllersDelta = null;
        if (appDelta != null)
            controllersDelta = appDelta.findMember(RailsNamingConventions.CONTROLLERS_PATH);
        if (appDelta != null && appDelta.getKind() == IResourceDelta.ADDED || controllersDelta != null
                && controllersDelta.getKind() == IResourceDelta.ADDED) {
            Assert.isTrue(rootFolder != null);
            db.setControllersFolderDelta(new ControllersFolderAddedDelta(rootFolder));
            return;
        }
        if (appDelta != null && appDelta.getKind() == IResourceDelta.REMOVED || controllersDelta != null
                && controllersDelta.getKind() == IResourceDelta.REMOVED) {
            if (oldRootFolder != null)
                db.setControllersFolderDelta(new ControllersFolderRemovedDelta(oldRootFolder));
            return;
        }
        
        if (rootFolder != null && controllersDelta != null) {
            ControllersFolderDelta fd = rootFolder.reconcile(controllersDelta);
            db.setControllersFolderDelta(fd);
        }
    }
    
    public IRailsProject getRailsProject() {
        return railsProject;
    }
    
    public IRailsController getApplicationController() {
        open();
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public IRailsControllersFolder getRootFolder() {
        open();
        return rootFolder;
    }
    
    private void open() {
        if (opened)
            return;
        Assert.isTrue(rootFolder == null);
        opened = true;
        if (root.exists())
            rootFolder = new RailsControllersFolder(this, root);
    }
    
    public void close() {
        if (!opened)
            return;
        opened = false;
        try {
            if (rootFolder != null)
                rootFolder.dispose();
        } finally {
            rootFolder = null;
        }
    }
    
}
