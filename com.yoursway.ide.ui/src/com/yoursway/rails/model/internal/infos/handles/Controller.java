package com.yoursway.rails.model.internal.infos.handles;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.models.controller.RailsController;
import com.yoursway.rails.models.core.internal.infos.InfoCore;
import com.yoursway.rails.models.project.RailsProject;

public class Controller {
    
    private final IFile file;
    private final Project parent;
    
    public Controller(Project parent, IFile file) {
        this.parent = parent;
        this.file = file;
    }
    
    /**
     * For Foo::BarController, returns {"Foo", "BarController"}.
     */
    public String[] getClassNameComponents() {
        return getInfo().getFullClassName();
    }
    
    /**
     * For Foo::BarController, returns {"foo", "bar_controller"},
     */
    public String[] getPathComponents() {
        return getInfo().getPathComponents();
    }
    
    public boolean exists() {
        return getInfo() != null;
    }
    
    RailsController getInfo() {
        RailsProject info = parent.getInfo();
        if (info != null)
            return InfoCore.CONTROLLER_INFOS.obtainActualInfo(info, file);
        return null;
    }
    
}
