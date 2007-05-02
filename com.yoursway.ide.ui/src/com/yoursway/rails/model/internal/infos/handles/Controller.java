package com.yoursway.rails.model.internal.infos.handles;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.model.internal.infos.ControllerInfo;
import com.yoursway.rails.model.internal.infos.InfoCore;
import com.yoursway.rails.model.internal.infos.ProjectInfo;

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
    
    ControllerInfo getInfo() {
        ProjectInfo info = parent.getInfo();
        if (info != null)
            return InfoCore.CONTROLLER_INFOS.obtainActualInfo(info, file);
        return null;
    }
    
}
