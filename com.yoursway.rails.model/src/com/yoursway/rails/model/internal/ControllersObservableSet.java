package com.yoursway.rails.model.internal;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.resources.IFile;

import com.yoursway.databinding.resources.ResourceObservables;
import com.yoursway.rails.commons.RailsFileUtils;
import com.yoursway.rails.commons.RailsNamingConventions;
import com.yoursway.rails.model.RailsController;
import com.yoursway.rails.model.RailsProject;

public class ControllersObservableSet extends MappedObservableSet<IFile, RailsController> {
    
    private final RailsProject railsProject;

    public ControllersObservableSet(Realm realm, RailsProject railsProject) {
        super(realm, RailsController.class, ResourceObservables.observeChildren(realm, 
                railsProject.getProject().getFolder(RailsNamingConventions.APP_CONTROLLERS_PATH)));
        this.railsProject = railsProject;
    }
    
    @Override
    protected RailsController createMapping(IFile file) {
        if (!RailsFileUtils.isRubyFile(file))
            return null;
        return new RailsController(railsProject, file);
    }
    
}
