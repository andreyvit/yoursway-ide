/**
 * 
 */
package com.yoursway.rails.core.fixtures.internal;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.core.fixtures.RailsFixture;
import com.yoursway.rails.core.internal.support.ComparingUpdater;
import com.yoursway.rails.core.projects.RailsProject;

public final class Requestor extends ComparingUpdater<IFile, IFile, RailsFixture> implements
        IRailsFixturesRequestor {
    
    private final RailsProject railsProject;
    
    public Requestor(RailsProject railsProject, Map<IFile, RailsFixture> oldItems) {
        super(oldItems);
        this.railsProject = railsProject;
    }
    
    @Override
    protected RailsFixture create(IFile key) {
        return new RailsFixture(railsProject, key);
    }
    
    @Override
    protected IFile getKey(IFile data) {
        return data;
    }
    
    @Override
    protected void update(RailsFixture value, IFile data) {
        Assert.isTrue(value.getFile().equals(data));
    }
    
}