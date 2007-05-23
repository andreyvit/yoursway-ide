/**
 * 
 */
package com.yoursway.rails.models.controller.internal;

import java.util.Map;

import org.eclipse.core.resources.IProject;

import com.yoursway.rails.models.ComparingUpdater;
import com.yoursway.rails.models.project.RailsProject;

public final class Requestor extends ComparingUpdater<IProject, RailsProject> implements
        IRailsProjectsRequestor {
    public Requestor(Map<IProject, RailsProject> oldItems) {
        super(oldItems);
    }
    
    @Override
    protected RailsProject create(IProject key) {
        return new RailsProject(key);
    }
    
}