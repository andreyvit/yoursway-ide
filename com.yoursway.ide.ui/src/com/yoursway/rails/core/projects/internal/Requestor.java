/**
 * 
 */
package com.yoursway.rails.core.projects.internal;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.core.internal.support.ComparingUpdater;
import com.yoursway.rails.core.projects.RailsProject;

public final class Requestor extends ComparingUpdater<IProject, IProject, RailsProject> implements
        IRailsProjectsRequestor {
    public Requestor(Map<IProject, RailsProject> oldItems) {
        super(oldItems);
    }
    
    @Override
    protected RailsProject create(IProject key) {
        return new RailsProject(key);
    }
    
    @Override
    protected IProject getKey(IProject data) {
        return data;
    }
    
    @Override
    protected void update(RailsProject value, IProject data) {
        Assert.isTrue(value.getProject().equals(data));
    }
    
}