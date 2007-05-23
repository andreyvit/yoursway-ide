package com.yoursway.rails.model.internal.infos.handles;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;

import com.yoursway.rails.models.controller.RailsController;
import com.yoursway.rails.models.core.internal.infos.InfoCore;
import com.yoursway.rails.models.project.RailsProject;

public class Project {
    
    private final IProject project;
    
    public Project(IProject project) {
        this.project = project;
    }
    
    public boolean exists() {
        return getInfo() != null;
    }
    
    RailsProject getInfo() {
        return InfoCore.PROJECT_INFOS.get(project);
    }
    
    public Collection<Controller> getControllers() throws ElementDoesNotExistException {
        final RailsProject info = getInfo();
        if (info == null)
            throw new ElementDoesNotExistException();
        Collection<Controller> result = new ArrayList<Controller>();
        for (RailsController railsController : InfoCore.CONTROLLER_INFOS.obtainActualInfos(info))
            result.add(new Controller(this, railsController.getFile()));
        return result;
    }
}
