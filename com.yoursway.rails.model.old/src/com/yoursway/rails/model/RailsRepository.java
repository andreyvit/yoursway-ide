package com.yoursway.rails.model;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.resources.IProject;

import com.yoursway.databinding.commons.YourSwayRealm;
import com.yoursway.rails.model.commands.CommandHistory;
import com.yoursway.rails.model.internal.ProjectsObservableSet;

public class RailsRepository {
    
    private final YourSwayRealm realm;
    
    private ProjectsObservableSet projectSet;

    public RailsRepository() {
        this.realm = new YourSwayRealm();
        projectSet = new ProjectsObservableSet(realm);
    }
    
    public IObservableSet observeProjects() {
        return projectSet;
    }
    
    public IObservableValue lookupProject(IProject project) {
        return new ProjectMapping(realm, project, projectSet);
    }
    
}