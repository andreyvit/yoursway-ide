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
import com.yoursway.rails.model.IRailsModel;
import com.yoursway.rails.model.IRailsModelsCollection;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.rails.model.internal.deltas.RailsProjectChangedDeltaBuilder;
import com.yoursway.utils.PathUtils;
import com.yoursway.utils.RailsFileUtils;

public class RailsModelsCollection extends RailsElement implements IRailsModelsCollection {
    
    private final IRailsProject railsProject;
    private Collection<RailsModel> models;
    private boolean itemsKnown = false;
    private IFolder modelsFolder;
    
    public RailsModelsCollection(IRailsProject railsProject) {
        this.railsProject = railsProject;
        modelsFolder = calculateModelsFolder();
    }
    
    public void refresh() {
        System.out.println("RailsModelsCollection.refresh()");
        models = new ArrayList<RailsModel>();
        itemsKnown = true;
        
        modelsFolder = calculateModelsFolder();
        if (modelsFolder == null || !modelsFolder.exists())
            return;
        locateModelsInFolder(modelsFolder);
    }
    
    public IFolder getModelsFolder() {
        return modelsFolder;
    }
    
    private IFolder calculateModelsFolder() {
        IProject project = railsProject.getProject();
        return PathUtils.getFolder(project, "app/models");
    }
    
    private void locateModelsInFolder(IFolder folder) {
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
                if (canAddModelFor(file))
                    addModel(null, file);
                break;
            case IResource.FOLDER:
                IFolder subfolder = (IFolder) resource;
                locateModelsInFolder(subfolder);
                break;
            }
        }
    }
    
    private boolean canAddModelFor(IFile file) {
        return RailsFileUtils.isRubyFile(file);
    }
    
    private RailsModel findModelFor(IFile file) {
        for (RailsModel model : models) {
            IFile cfile = model.getCorrespondingFile();
            if (cfile.equals(file))
                return model;
        }
        return null;
    }
    
    private void addModel(RailsProjectChangedDeltaBuilder db, IFile file) {
        RailsModel railsModel = new RailsModel(this, file);
        models.add(railsModel);
    }
    
    public Collection<? extends IRailsModel> getItems() {
        open();
        return models;
    }
    
    private void open() {
        if (!itemsKnown)
            refresh();
    }
    
    public void reconcile(RailsProjectChangedDeltaBuilder db, IResourceDelta delta) {
        open();
        IResourceDelta folderDelta = delta.findMember(new Path("app/models"));
        if (folderDelta == null)
            return;
        switch (folderDelta.getKind()) {
        case IResourceDelta.ADDED:
        case IResourceDelta.REMOVED:
            refresh();
            break;
        case IResourceDelta.CHANGED:
            reconcileChildDeltas(db, delta);
            break;
        }
    }
    
    private void reconcileChildDeltas(RailsProjectChangedDeltaBuilder db, IResourceDelta parentDelta) {
        for (IResourceDelta delta : parentDelta.getAffectedChildren()) {
            IResource resource = delta.getResource();
            switch (delta.getKind()) {
            case IResourceDelta.ADDED:
                switch (resource.getType()) {
                case IResource.FILE:
                    if (canAddModelFor((IFile) resource))
                        addModel(db, (IFile) resource);
                    break;
                case IResource.FOLDER:
                    locateModelsInFolder((IFolder) resource);
                    break;
                }
                break;
            case IResourceDelta.REMOVED:
                switch (resource.getType()) {
                case IResource.FILE:
                    IRailsModel controller = findModelFor((IFile) resource);
                    if (controller != null)
                        removeModel(db, controller, (IFile) resource);
                    break;
                case IResource.FOLDER:
                    locateModelsInFolder((IFolder) resource);
                    break;
                }
                break;
            case IResourceDelta.CHANGED:
                switch (resource.getType()) {
                case IResource.FILE:
                    RailsModel model = findModelFor((IFile) resource);
                    if (model != null)
                        model.reconcile(db, delta);
                    break;
                case IResource.FOLDER:
                    reconcileChildDeltas(db, delta);
                    break;
                }
                break;
            }
        }
    }
    
    private void removeModel(RailsProjectChangedDeltaBuilder db, IRailsModel model, IFile resource) {
        models.remove(model);
    }
    
    public boolean isEmpty() {
        return getModelsFolder() == null;
    }
    
    public IRailsProject getRailsProject() {
        return railsProject;
    }
    
}
