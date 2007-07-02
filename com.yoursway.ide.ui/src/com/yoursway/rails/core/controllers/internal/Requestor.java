/**
 * 
 */
package com.yoursway.rails.core.controllers.internal;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.core.controllers.RailsController;
import com.yoursway.rails.core.internal.support.ComparingUpdater;
import com.yoursway.rails.core.projects.RailsProject;

public final class Requestor extends ComparingUpdater<IFile, IFile, RailsController> implements
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
    
    @Override
    protected IFile getKey(IFile data) {
        return data;
    }
    
    @Override
    protected void update(RailsController value, IFile data) {
        Assert.isTrue(value.getFile().equals(data));
    }
    
}