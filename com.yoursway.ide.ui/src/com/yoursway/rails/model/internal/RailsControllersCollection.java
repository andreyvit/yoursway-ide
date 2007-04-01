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

public class RailsControllersCollection implements IRailsControllersCollection {
    
    private Collection<RailsController> controllers;
    private final IRailsProject railsProject;
    
    public RailsControllersCollection(IRailsProject railsProject) {
        this.railsProject = railsProject;
        refresh();
    }
    
    public void refresh() {
        IProject project = railsProject.getProject();
        IResource controllersFolder = project.findMember("app/controllers");
        Collection<RailsController> newControllers = controllers = new ArrayList<RailsController>();
        if (controllersFolder != null && controllersFolder.getType() == IResource.FOLDER) {
            if (controllersFolder.exists() && controllersFolder.getType() == IResource.FOLDER)
                locateControllersInFolder(newControllers, (IFolder) controllersFolder);
        }
    }
    
    private void locateControllersInFolder(Collection<RailsController> newControllers, IFolder folder) {
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
                if (canAddControllerFor(file)) {
                    addController(null, newControllers, file);
                }
                break;
            case IResource.FOLDER:
                // TODO: handle namespaces
                locateControllersInFolder(newControllers, (IFolder) resource);
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
    
    private void addController(RailsDeltaBuilder deltaBuilder, Collection<RailsController> newControllers,
            IFile file) {
        RailsController railsController = new RailsController(this, file);
        newControllers.add(railsController);
        if (deltaBuilder != null)
            deltaBuilder.somethingChanged();
    }
    
    public Collection<? extends IRailsController> getItems() {
        return controllers;
    }
    
    public void reconcile(RailsDeltaBuilder deltaBuilder, IResourceDelta delta) {
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
                        addController(deltaBuilder, controllers, (IFile) resource);
                    break;
                case IResource.FOLDER:
                    locateControllersInFolder(controllers, (IFolder) resource);
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
                    locateControllersInFolder(controllers, (IFolder) resource);
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
    
}
