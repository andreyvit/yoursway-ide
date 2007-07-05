/**
 * 
 */
package com.yoursway.rails.core.tests.internal;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.core.internal.support.ComparingUpdater;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.rails.core.tests.RailsTest;

public final class Requestor extends ComparingUpdater<IFile, IFile, RailsTest> implements
        IRailsTestsRequestor {
    
    private final RailsProject railsProject;
    
    public Requestor(RailsProject railsProject, Map<IFile, RailsTest> oldItems) {
        super(oldItems);
        this.railsProject = railsProject;
    }
    
    @Override
    protected RailsTest create(IFile key) {
        return new RailsTest(railsProject, key);
    }
    
    @Override
    protected IFile getKey(IFile data) {
        return data;
    }
    
    @Override
    protected void update(RailsTest value, IFile data) {
        Assert.isTrue(value.getFile().equals(data));
    }
    
}