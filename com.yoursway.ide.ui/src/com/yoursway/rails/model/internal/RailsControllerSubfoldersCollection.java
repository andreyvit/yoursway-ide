package com.yoursway.rails.model.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.model.IRailsControllerSubfoldersCollection;
import com.yoursway.rails.model.IRailsControllersFolder;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.rails.model.deltas.controllers.ControllersFolderAddedDelta;
import com.yoursway.rails.model.deltas.controllers.ControllersFolderDelta;
import com.yoursway.rails.model.deltas.controllers.ControllersFolderRemovedDelta;
import com.yoursway.rails.model.internal.deltas.RailsControllersFolderChangeDeltaBuilder;
import com.yoursway.utils.ResourceDeltaSwitch;

public class RailsControllerSubfoldersCollection extends RailsElement implements
        IRailsControllerSubfoldersCollection {
    
    private Map<IFolder, RailsControllersFolder> subfolders;
    private boolean itemsKnown = false;
    private final IRailsControllersFolder parent;
    private final IFolder folder;
    
    private class DeltaProcessor extends ResourceDeltaSwitch {
        
        private final RailsControllersFolderChangeDeltaBuilder deltaBuilder;
        
        public DeltaProcessor(RailsControllersFolderChangeDeltaBuilder deltaBuilder) {
            this.deltaBuilder = deltaBuilder;
        }
        
        @Override
        protected void visitAddedDelta(IResourceDelta delta) {
            add((IFolder) delta.getResource());
        }
        
        @Override
        protected void visitChangedDelta(IResourceDelta delta) {
            changed((IFolder) delta.getResource(), delta);
        }
        
        @Override
        protected void visitRemovedDelta(IResourceDelta delta) {
            removed((IFolder) delta.getResource());
        }
        
        public void add(IFolder subfolder) {
            RailsControllersFolder existingFolder = subfolders.get(subfolder);
            if (existingFolder != null)
                removed(existingFolder);
            RailsControllersFolder railsFolder = new RailsControllersFolder(parent, folder);
            subfolders.put(subfolder, railsFolder);
            if (deltaBuilder != null)
                deltaBuilder.addChildFolderDelta(new ControllersFolderAddedDelta(railsFolder));
        }
        
        public void changed(IFolder subfolder, IResourceDelta delta) {
            RailsControllersFolder existingFolder = subfolders.get(subfolder);
            if (existingFolder == null)
                add(subfolder);
            else {
                ControllersFolderDelta childDelta = existingFolder.reconcile(delta);
                if (deltaBuilder != null)
                    deltaBuilder.addChildFolderDelta(childDelta);
            }
        }
        
        public void removed(IFolder subfolder) {
            RailsControllersFolder railsFolder = subfolders.remove(subfolder);
            if (railsFolder == null)
                return; // must have missed an addition
            removed(railsFolder);
        }
        
        private void removed(RailsControllersFolder railsFolder) {
            if (deltaBuilder != null)
                deltaBuilder.addChildFolderDelta(new ControllersFolderRemovedDelta(railsFolder));
        }
    }
    
    public RailsControllerSubfoldersCollection(IRailsControllersFolder parent, IFolder folder) {
        this.parent = parent;
        this.folder = folder;
    }
    
    public void refresh() {
        close();
        open();
    }
    
    private void open() {
        if (itemsKnown)
            return;
        itemsKnown = true;
        subfolders = new HashMap<IFolder, RailsControllersFolder>();
        IResource[] members;
        try {
            members = folder.members();
        } catch (CoreException e) {
            Activator.log(e);
            return;
        }
        DeltaProcessor deltaProcessor = new DeltaProcessor(null);
        for (IResource resource : members) {
            if (resource.getType() == IResource.FOLDER) {
                IFolder subfolder = (IFolder) resource;
                deltaProcessor.add(subfolder);
            }
        }
    }
    
    public void close() {
        if (!itemsKnown)
            return;
        subfolders = null;
        itemsKnown = false;
    }
    
    public void reconcile(RailsControllersFolderChangeDeltaBuilder deltaBuilder,
            IResourceDelta thisFolderDelta) {
        Assert.isLegal(thisFolderDelta.getKind() == IResourceDelta.CHANGED);
        open();
        DeltaProcessor deltaProcessor = new DeltaProcessor(deltaBuilder);
        IResourceDelta[] childDeltas = thisFolderDelta.getAffectedChildren();
        for (IResourceDelta childDelta : childDeltas)
            deltaProcessor.visit(childDelta);
    }
    
    public IRailsProject getRailsProject() {
        return parent.getRailsProject();
    }
    
    public Collection<? extends IRailsControllersFolder> getSubfolders() {
        open();
        return subfolders.values();
    }
    
    public boolean hasSubfolders() {
        return true;
    }
    
}
