package com.yoursway.rails.model.internal;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

import com.yoursway.databinding.resources.ResourceObservables;
import com.yoursway.rails.model.RailsProject;

public class ProjectsObservableSet extends MappedObservableSet<IProject, RailsProject> {
    
    public ProjectsObservableSet(Realm realm) {
        super(realm, RailsProject.class, ResourceObservables.observeChildren(realm, ResourcesPlugin.getWorkspace().getRoot()));
    }
    
    @Override
    protected RailsProject createMapping(IProject project) {
        // FIXME check if this is a Rails project or not
//        if (!project.getFolder("app").exists())
//            return null;
        return new RailsProject(getRealm(), project);
    }
    
}
