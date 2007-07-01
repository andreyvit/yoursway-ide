package com.yoursway.rails.core.models;

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
import com.yoursway.rails.core.models.internal.BroadcastingRailsModelsChangeVisitor;
import com.yoursway.rails.core.models.internal.RailsModelsIterator;
import com.yoursway.rails.core.models.internal.RailsModelsRequestor;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.utils.RailsNamingConventions;

public class PerProjectRailsModelsCollection extends AbstractModel<IModelsListener> {
    
    private Map<IFile, RailsModel> items = new HashMap<IFile, RailsModel>();
    private final RailsProject railsProject;
    
    public PerProjectRailsModelsCollection(RailsProject railsProject) {
        this.railsProject = railsProject;
        rebuild();
    }
    
    public RailsProject getRailsProject() {
        return railsProject;
    }
    
    public Collection<RailsModel> getAll() {
        return items.values();
    }
    
    public RailsModel get(IFile project) {
        return items.get(project);
    }
    
    public void rebuild() {
        RailsModelsRequestor updater = new RailsModelsRequestor(railsProject, items);
        new RailsModelsIterator(railsProject, updater).build();
        items = updater.getNewItems();
        updater.visitChanges(new BroadcastingRailsModelsChangeVisitor(getListeners()));
    }
    
    @Override
    protected IModelsListener[] makeListenersArray(int size) {
        return new IModelsListener[size];
    }
    
    public void reconcile(IResourceDelta projectRD) {
        rebuild();
        IResourceDelta folderDelta = projectRD.findMember(RailsNamingConventions.APP_MODELS_PATH);
        if (folderDelta == null)
            return;
        try {
            folderDelta.accept(new IResourceDeltaVisitor() {
                
                public boolean visit(IResourceDelta delta) throws CoreException {
                    if (delta.getResource().getType() == IResource.FILE) {
                        IFile file = (IFile) delta.getResource();
                        RailsModel railsModel = items.get(file);
                        if (railsModel != null)
                            for (IModelsListener listener : getListeners())
                                listener.modelContentChanged(railsModel);
                    }
                    return true;
                }
                
            });
        } catch (CoreException e) {
            Activator.unexpectedError(e);
        }
    }
}
