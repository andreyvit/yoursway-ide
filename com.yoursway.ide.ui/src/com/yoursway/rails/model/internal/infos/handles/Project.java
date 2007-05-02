package com.yoursway.rails.model.internal.infos.handles;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;

import com.yoursway.rails.model.internal.infos.ControllerInfo;
import com.yoursway.rails.model.internal.infos.InfoCore;
import com.yoursway.rails.model.internal.infos.ProjectInfo;

public class Project {
    
    private final IProject project;
    
    public Project(IProject project) {
        this.project = project;
    }
    
    public boolean exists() {
        return getInfo() != null;
    }
    
    ProjectInfo getInfo() {
        return InfoCore.PROJECT_INFOS.obtainActualInfo(project);
    }
    
    public Collection<Controller> getControllers() throws ElementDoesNotExistException {
        final ProjectInfo info = getInfo();
        if (info == null)
            throw new ElementDoesNotExistException();
        Collection<Controller> result = new ArrayList<Controller>();
        for (ControllerInfo controllerInfo : InfoCore.CONTROLLER_INFOS.obtainActualInfos(info))
            result.add(new Controller(this, controllerInfo.getFile()));
        return result;
    }
}
