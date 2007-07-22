package com.yoursway.rails.model;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.resources.IProject;

import com.yoursway.rails.model.internal.ProjectsObservableSet;

public class ProjectMapping extends ComputedValue {

    private final ProjectsObservableSet theSet;
    private final IProject project;

    public ProjectMapping(Realm realm, IProject project, ProjectsObservableSet theSet) {
        super(realm, RailsProject.class);
        this.project = project;
        this.theSet = theSet;
    }

    @Override
    protected Object calculate() {
        return theSet.lookup(project);
    }
    
}
