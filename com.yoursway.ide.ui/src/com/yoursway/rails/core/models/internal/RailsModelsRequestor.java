/**
 * 
 */
package com.yoursway.rails.core.models.internal;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.core.internal.support.ComparingUpdater;
import com.yoursway.rails.core.models.RailsModel;
import com.yoursway.rails.core.projects.RailsProject;

public final class RailsModelsRequestor extends ComparingUpdater<IFile, IFile, RailsModel> implements
        IRailsModelsRequestor {
    
    private final RailsProject railsProject;
    
    public RailsModelsRequestor(RailsProject railsProject, Map<IFile, RailsModel> oldItems) {
        super(oldItems);
        this.railsProject = railsProject;
    }
    
    @Override
    protected RailsModel create(IFile key) {
        return new RailsModel(railsProject, key);
    }
    
    @Override
    protected IFile getKey(IFile data) {
        return data;
    }
    
    @Override
    protected void update(RailsModel value, IFile data) {
        Assert.isTrue(value.getFile().equals(data));
    }
    
}