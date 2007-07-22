package com.yoursway.rails.model;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.resources.IProject;

import com.yoursway.rails.model.internal.ProjectsObservableSet;

public class RailsRepository {
    
    private final Realm realm;
    
    private WeakReference<ProjectsObservableSet> projectSet;

    public RailsRepository(Realm realm) {
        this.realm = realm;
    }
    
    public IObservableSet observeProjects() {
        return obtainProjectSet();
    }

    private synchronized ProjectsObservableSet obtainProjectSet() {
        ProjectsObservableSet result = null;
        if (projectSet != null)
            result = projectSet.get();
        if (result == null) {
            result = new ProjectsObservableSet(realm);
            projectSet = new WeakReference<ProjectsObservableSet>(result);
        }
        return result;
    }
    
    public IObservableValue lookupProject(IProject project) {
        return new ProjectMapping(realm, project, obtainProjectSet());
    }
    
}
