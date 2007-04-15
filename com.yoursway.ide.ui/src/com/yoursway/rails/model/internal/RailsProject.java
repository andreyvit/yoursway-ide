package com.yoursway.rails.model.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;

import com.yoursway.rails.model.IRailsControllersCollection;
import com.yoursway.rails.model.IRailsModelsCollection;
import com.yoursway.rails.model.IRailsProject;

public class RailsProject implements IRailsProject {
    
    private final IProject project;
    
    private RailsControllersCollection controllers;
    
    private RailsModelsCollection models;
    
    private final RailsProjectsCollection parent;
    
    public RailsProject(RailsProjectsCollection parent, IProject project) {
        this.parent = parent;
        this.project = project;
    }
    
    public void refresh() {
        System.out.println("RailsProject.refresh()");
        controllers.refresh();
    }
    
    public IProject getProject() {
        return project;
    }
    
    public IRailsControllersCollection getControllersCollection() {
        if (controllers == null) {
            controllers = new RailsControllersCollection(this);
        }
        return controllers;
    }
    
    public void reconcile(RailsDeltaBuilder deltaBuilder, IResourceDelta delta) {
        ((RailsControllersCollection) getControllersCollection()).reconcile(deltaBuilder, delta);
        ((RailsModelsCollection) getModelsCollection()).reconcile(deltaBuilder, delta);
    }
    
    public IRailsProject getRailsProject() {
        return this;
    }
    
    public IRailsModelsCollection getModelsCollection() {
        if (models == null) {
            models = new RailsModelsCollection(this);
        }
        return models;
    }
    
}
