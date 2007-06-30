/**
 * 
 */
package com.yoursway.rails.models.controller.internal;

import java.util.Map;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.models.ComparingUpdater;
import com.yoursway.rails.models.controller.RailsController;
import com.yoursway.rails.models.project.RailsProject;

public final class Requestor extends ComparingUpdater<IFile, RailsController> implements
        IRailsControllersRequestor {
    
    private final RailsProject railsProject;
    
    public Requestor(RailsProject railsProject, Map<IFile, RailsController> oldItems) {
        super(oldItems);
        this.railsProject = railsProject;
    }
    
    @Override
    protected RailsController create(IFile key) {
        return new RailsController(railsProject, key);
    }
    
}