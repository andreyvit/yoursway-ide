package com.yoursway.rails.model.internal;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResourceDelta;

import com.yoursway.rails.model.IRailsControllerSubfoldersCollection;
import com.yoursway.rails.model.IRailsControllersManager;
import com.yoursway.rails.model.IRailsControllersFolder;
import com.yoursway.rails.model.IRailsFolderControllersCollection;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.rails.model.deltas.controllers.ControllersFolderDelta;
import com.yoursway.rails.model.internal.deltas.RailsControllersFolderChangeDeltaBuilder;

public class RailsControllersFolder extends RailsElement implements IRailsControllersFolder {
    
    private final IRailsControllersManager parentManager;
    private final IFolder folder;
    private final RailsControllerSubfoldersCollection subfoldersCollection;
    private final RailsFolderControllersCollection controllersCollection;
    private final IRailsControllersFolder parentFolder;
    
    public RailsControllersFolder(IRailsControllersFolder parentFolder, IFolder folder) {
        this(parentFolder.getControllersManager(), parentFolder, folder);
    }
    
    public RailsControllersFolder(IRailsControllersManager parent, IFolder folder) {
        this(parent, null, folder);
    }
    
    private RailsControllersFolder(IRailsControllersManager parent, IRailsControllersFolder parentFolder,
            IFolder folder) {
        this.parentManager = parent;
        this.parentFolder = parentFolder;
        this.folder = folder;
        subfoldersCollection = new RailsControllerSubfoldersCollection(this, folder);
        controllersCollection = new RailsFolderControllersCollection(this, folder);
    }
    
    public void dispose() {
        //
    }
    
    public void refresh() {
        subfoldersCollection.refresh();
        controllersCollection.refresh();
    }
    
    public ControllersFolderDelta reconcile(IResourceDelta delta) {
        RailsControllersFolderChangeDeltaBuilder db = new RailsControllersFolderChangeDeltaBuilder(this);
        subfoldersCollection.reconcile(db, delta);
        controllersCollection.reconcile(db, delta);
        return db.build();
    }
    
    public IRailsProject getRailsProject() {
        return parentManager.getRailsProject();
    }
    
    public IRailsFolderControllersCollection getControllersCollection() {
        return controllersCollection;
    }
    
    public IRailsControllerSubfoldersCollection getSubfoldersCollection() {
        return subfoldersCollection;
    }
    
    public IRailsControllersManager getControllersManager() {
        return parentManager;
    }
    
    public IRailsControllersFolder getParentFolder() {
        return parentFolder;
    }
    
    public IFolder getCorrespondingFolder() {
        return folder;
    }
    
}
