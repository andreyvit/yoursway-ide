package com.yoursway.rails.models.core.internal.infos;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.models.controller.RailsController;
import com.yoursway.rails.models.project.RailsProject;

public class ComparingControllerInfoRequestor implements IControllerInfoRequestor {
    
    private final IControllerInfoHolder holder;
    
    private final Set<RailsController> actualInfos = new HashSet<RailsController>();
    
    private boolean finalized = false;
    
    public ComparingControllerInfoRequestor(IControllerInfoHolder holder) {
        this.holder = holder;
    }
    
    public void consumeInfo(RailsProject parentProject, IFile file) {
        checkNotFinalized();
        RailsController info = holder.get(file);
        if (info == null) {
            info = new RailsController(parentProject, file);
            holder.add(info);
        }
        actualInfos.add(info);
    }
    
    public void removeStale() {
        checkNotFinalized();
        finalized = true;
        HashSet<RailsController> items = new HashSet<RailsController>(holder.values());
        items.removeAll(actualInfos);
        holder.removeAll(items);
    }
    
    private void checkNotFinalized() {
        if (finalized)
            throw new IllegalStateException();
    }
    
}
