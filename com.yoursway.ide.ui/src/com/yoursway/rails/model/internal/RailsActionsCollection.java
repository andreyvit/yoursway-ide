package com.yoursway.rails.model.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.model.IRailsController;
import com.yoursway.rails.model.IRailsControllersCollection;
import com.yoursway.rails.model.IRailsProject;

public class RailsActionsCollection implements IRailsControllersCollection {
    
    private final IRailsProject railsProject;
    private Collection<RailsController> controllers;
    private boolean itemsKnown = false;
    private IFolder controllersFolder;
    
    public RailsActionsCollection(IRailsProject railsProject) {
        this.railsProject = railsProject;
    }
    
    public void refresh() {
        System.out.println("RailsControllersCollection.refresh()");
        controllers = new ArrayList<RailsController>();
        IFolder controllersFolder = getControllersFolder();
        if (controllersFolder != null)
            locateControllersInFolder(controllersFolder);
        itemsKnown = true;
    }
    
    public IFolder getControllersFolder() {
        // XXX cache when this does not exist
        if (controllersFolder == null)
            controllersFolder = calculateControllersFolder();
        else if (!controllersFolder.exists())
            controllersFolder = calculateControllersFolder();
        return controllersFolder;
    }
    
    private IFolder calculateControllersFolder() {
        IProject project = railsProject.getProject();
        IResource controllersResource = project.findMember("app/controllers");
        if (controllersResource != null && controllersResource.getType() == IResource.FOLDER) {
            if (controllersResource.exists())
                return (IFolder) controllersResource;
        }
        return null;
    }
    
    private void locateControllersInFolder(IFolder folder) {
        IResource[] members;
        try {
            members = folder.members();
        } catch (CoreException e) {
            Activator.log(e);
            return; // TODO
        }
        for (IResource resource : members) {
            switch (resource.getType()) {
            case IResource.FILE:
                IFile file = (IFile) resource;
                if (canAddControllerFor(file))
                    addController(null, file);
                break;
            case IResource.FOLDER:
                IFolder subfolder = (IFolder) resource;
                locateControllersInFolder(subfolder);
                break;
            }
        }
    }
    
    private boolean canAddControllerFor(IFile file) {
        return RailsFileUtils.isRubyFile(file);
    }
    
    private RailsController findControllerFor(IFile file) {
        for (RailsController controller : controllers) {
            IFile cfile = controller.getFile();
            if (cfile.equals(file))
                return controller;
        }
        return null;
    }
    
    private void addController(RailsDeltaBuilder deltaBuilder, IFile file) {
        RailsController railsController = new RailsController(this, file);
        controllers.add(railsController);
        if (deltaBuilder != null)
            deltaBuilder.somethingChanged();
    }
    
    public Collection<? extends IRailsController> getItems() {
        open();
        return controllers;
    }
    
    private void open() {
        if (!itemsKnown)
            refresh();
    }
    
    public void reconcile(RailsDeltaBuilder deltaBuilder, IResourceDelta delta) {
        controllersFolder = null;
        open();
        IResourceDelta folderDelta = delta.findMember(new Path("app/controllers"));
        if (folderDelta == null)
            return;
        switch (folderDelta.getKind()) {
        case IResourceDelta.ADDED:
        case IResourceDelta.REMOVED:
            refresh();
            break;
        case IResourceDelta.CHANGED:
            reconcileChildDeltas(deltaBuilder, delta);
            break;
        }
    }
    
    private void reconcileChildDeltas(RailsDeltaBuilder deltaBuilder, IResourceDelta parentDelta) {
        for (IResourceDelta delta : parentDelta.getAffectedChildren()) {
            IResource resource = delta.getResource();
            switch (delta.getKind()) {
            case IResourceDelta.ADDED:
                switch (resource.getType()) {
                case IResource.FILE:
                    if (canAddControllerFor((IFile) resource))
                        addController(deltaBuilder, (IFile) resource);
                    break;
                case IResource.FOLDER:
                    locateControllersInFolder((IFolder) resource);
                    break;
                }
                break;
            case IResourceDelta.REMOVED:
                switch (resource.getType()) {
                case IResource.FILE:
                    IRailsController controller = findControllerFor((IFile) resource);
                    if (controller != null)
                        removeController(deltaBuilder, controller, (IFile) resource);
                    break;
                case IResource.FOLDER:
                    locateControllersInFolder((IFolder) resource);
                    break;
                }
                break;
            case IResourceDelta.CHANGED:
                switch (resource.getType()) {
                case IResource.FILE:
                    RailsController controller = findControllerFor((IFile) resource);
                    if (controller != null)
                        controller.reconcile(deltaBuilder, delta);
                    break;
                case IResource.FOLDER:
                    reconcileChildDeltas(deltaBuilder, delta);
                    break;
                }
                break;
            }
        }
    }
    
    private void removeController(RailsDeltaBuilder deltaBuilder, IRailsController controller, IFile resource) {
        controllers.remove(controller);
        if (deltaBuilder != null)
            deltaBuilder.somethingChanged();
    }
    
    public boolean isEmpty() {
        return getControllersFolder() == null;
    }
    
    public IRailsProject getRailsProject() {
        return railsProject;
    }
    
}
