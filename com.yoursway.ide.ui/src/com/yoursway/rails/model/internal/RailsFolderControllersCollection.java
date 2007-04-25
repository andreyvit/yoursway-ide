package com.yoursway.rails.model.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.model.IRailsController;
import com.yoursway.rails.model.IRailsControllersFolder;
import com.yoursway.rails.model.IRailsFolderControllersCollection;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.rails.model.deltas.controllers.ControllerAddedDelta;
import com.yoursway.rails.model.deltas.controllers.ControllerChangedDelta;
import com.yoursway.rails.model.deltas.controllers.ControllerRemovedDelta;
import com.yoursway.rails.model.internal.deltas.RailsControllersFolderChangeDeltaBuilder;
import com.yoursway.utils.RailsFileUtils;
import com.yoursway.utils.ResourceDeltaSwitch;

public class RailsFolderControllersCollection extends RailsElement implements
        IRailsFolderControllersCollection {
    
    private Map<IFile, RailsController> controllers;
    private boolean opened = false;
    private final IFolder folder;
    private final IRailsControllersFolder parent;
    
    private class DeltaProcessor extends ResourceDeltaSwitch {
        
        private final RailsControllersFolderChangeDeltaBuilder deltaBuilder;
        
        public DeltaProcessor(RailsControllersFolderChangeDeltaBuilder deltaBuilder) {
            this.deltaBuilder = deltaBuilder;
        }
        
        @Override
        protected void visitAddedDelta(IResourceDelta delta) {
            added((IFile) delta.getResource());
        }
        
        @Override
        protected void visitChangedDelta(IResourceDelta delta) {
            changed((IFile) delta.getResource(), delta);
        }
        
        @Override
        protected void visitRemovedDelta(IResourceDelta delta) {
            removed((IFile) delta.getResource());
        }
        
        public void added(IFile file) {
            RailsController existingController = controllers.get(file);
            if (existingController != null)
                removed(existingController);
            RailsController railsController = new RailsController(parent, file);
            controllers.put(file, railsController);
            if (deltaBuilder != null)
                deltaBuilder.addControllerDelta(new ControllerAddedDelta(railsController));
        }
        
        public void changed(IFile file, IResourceDelta delta) {
            RailsController railsController = controllers.get(file);
            if (railsController == null)
                added(file);
            else {
                ControllerChangedDelta childDelta = railsController.reconcile(delta);
                if (deltaBuilder != null)
                    deltaBuilder.addControllerDelta(childDelta);
            }
        }
        
        public void removed(IFile existingController) {
            RailsController railsFolder = controllers.remove(existingController);
            if (railsFolder == null)
                // must have missed an addition
                return;
            removed(railsFolder);
        }
        
        private void removed(RailsController railsFolder) {
            if (deltaBuilder != null)
                deltaBuilder.addControllerDelta(new ControllerRemovedDelta(railsFolder));
        }
    }
    
    public RailsFolderControllersCollection(IRailsControllersFolder parent, IFolder folder) {
        this.parent = parent;
        this.folder = folder;
    }
    
    public void refresh() {
        close();
        open();
    }
    
    private void open() {
        if (opened)
            return;
        opened = true;
        controllers = new HashMap<IFile, RailsController>();
        IResource[] members;
        try {
            members = folder.members();
        } catch (CoreException e) {
            Activator.log(e);
            return;
        }
        DeltaProcessor deltaProcessor = new DeltaProcessor(null);
        for (IResource resource : members) {
            if (resource.getType() == IResource.FILE) {
                IFile file = (IFile) resource;
                deltaProcessor.added(file);
            }
        }
    }
    
    public void close() {
        if (!opened)
            return;
        controllers = null;
        opened = false;
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
    
    private boolean canAddControllerFor(IFile file) {
        return RailsFileUtils.isRubyFile(file);
    }
    
    public IRailsProject getRailsProject() {
        return parent.getRailsProject();
    }
    
    public Collection<? extends IRailsController> getControllers() {
        open();
        return controllers.values();
    }
    
    public boolean hasControllers() {
        return true;
    }
    
}
