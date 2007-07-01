package com.yoursway.rails.core.models.internal;

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

public class RailsModelsIterator {
    
    private final IRailsModelsRequestor requestor;
    private final RailsProject railsProject;
    
    public RailsModelsIterator(RailsProject railsProject, IRailsModelsRequestor requestor) {
        this.railsProject = railsProject;
        Assert.isLegal(requestor != null);
        this.requestor = requestor;
    }
    
    public void build() {
        IResource appControllers = railsProject.getProject().findMember(RailsNamingConventions.APP_MODELS);
        if (appControllers == null)
            return;
        try {
            appControllers.accept(new ResourceSwitch() {
                
                @Override
                protected boolean visitFile(IFile resource) {
                    if (isModel(resource))
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
    
    private static boolean isModel(IFile file) {
        return RailsFileUtils.isRubyFile(file);
    }
    
}
