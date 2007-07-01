package com.yoursway.rails.core.controllers.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.utils.RailsFileUtils;
import com.yoursway.utils.RailsNamingConventions;
import com.yoursway.utils.ResourceSwitch;

public class RailsControllersIterator {
    
    private final IRailsControllersRequestor requestor;
    private final RailsProject railsProject;
    
    public RailsControllersIterator(RailsProject railsProject, IRailsControllersRequestor requestor) {
        this.railsProject = railsProject;
        Assert.isLegal(requestor != null);
        this.requestor = requestor;
    }
    
    public void build() {
        IResource appControllers = railsProject.getProject().findMember(
                RailsNamingConventions.APP_CONTROLLERS_PATH);
        if (appControllers == null)
            return;
        try {
            appControllers.accept(new ResourceSwitch() {
                
                @Override
                protected boolean visitFile(IFile resource) {
                    if (isController(resource))
                        requestor.accept(resource);
                    return false;
                }
                
                @Override
                protected boolean visitFolder(IFolder resource) {
                    return true;
                }
                
            });
        } catch (CoreException e) {
            Activator.unexpectedError(e);
        }
    }
    
    private static boolean isController(IFile file) {
        return RailsFileUtils.isRubyFile(file);
    }
    
}
