package com.yoursway.rails.model.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.debug.core.ILaunch;

import com.yoursway.rails.model.IRailsControllersManager;
import com.yoursway.rails.model.IRailsModelsCollection;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.rails.model.IRailsSchema;
import com.yoursway.rails.model.internal.deltas.RailsProjectChangedDeltaBuilder;
import com.yoursway.utils.Inflector;
import com.yoursway.utils.RailsNamingConventions;

public class RailsProject extends RailsElement implements IRailsProject {
    
    private final IProject project;
    
    private RailsControllersCollection controllers;
    
    private RailsModelsCollection models;
    
    private RailsSchema tables;
    
    private final RailsProjectsCollection parent;
    
    private Inflector inflector;
    
    private ILaunch launch;
    
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
    
    public IRailsControllersManager getControllersCollection() {
        if (controllers == null) {
            controllers = new RailsControllersCollection(this);
        }
        return controllers;
    }
    
    public void reconcile(RailsProjectChangedDeltaBuilder db, IResourceDelta delta) {
        ((RailsControllersCollection) getControllersCollection()).reconcile(db, delta);
        ((RailsModelsCollection) getModelsCollection()).reconcile(db, delta);
        ((RailsSchema) getSchema()).reconcile(db, delta);
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
    
    public IRailsSchema getSchema() {
        if (tables == null) {
            tables = new RailsSchema(this);
        }
        return tables;
    }
    
    public Inflector getInflector() {
        if (inflector == null) {
            inflector = RailsNamingConventions.createInitializedInflector();
        }
        return inflector;
    }
    
}
