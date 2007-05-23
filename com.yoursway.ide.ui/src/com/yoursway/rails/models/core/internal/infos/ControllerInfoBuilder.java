package com.yoursway.rails.models.core.internal.infos;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.models.project.RailsProject;
import com.yoursway.utils.RailsFileUtils;
import com.yoursway.utils.RailsNamingConventions;
import com.yoursway.utils.ResourceSwitch;

public class ControllerInfoBuilder {
    
    private final class CollectingVisitor extends ResourceSwitch {
        @Override
        protected boolean visitFile(IFile resource) {
            if (canAddControllerFor(resource))
                addController(resource);
            return false;
        }
        
        @Override
        protected boolean visitFolder(IFolder resource) {
            return true;
        }
        
        @Override
        protected boolean visitProject(IProject resource) {
            throw new AssertionError("Cannot happen");
        }
        
        @Override
        protected boolean visitRoot(IWorkspaceRoot resource) {
            throw new AssertionError("Cannot happen");
        }
    }
    
    private final RailsProject railsProject;
    private final IControllerInfoRequestor infoRequestor;
    
    public ControllerInfoBuilder(RailsProject railsProject, IControllerInfoRequestor infoRequestor) {
        Assert.isLegal(railsProject != null);
        this.railsProject = railsProject;
        this.infoRequestor = infoRequestor;
    }
    
    public void build() {
        IFolder controllersFolder = railsProject.getProject().getFolder(
                RailsNamingConventions.APP_CONTROLLERS_PATH);
        if (controllersFolder.exists())
            try {
                controllersFolder.accept(new CollectingVisitor());
            } catch (CoreException e) {
                Activator.unexpectedError(e);
            }
    }
    
    private boolean canAddControllerFor(IFile file) {
        return RailsFileUtils.isRubyFile(file);
    }
    
    private void addController(IFile file) {
        infoRequestor.consumeInfo(railsProject, file);
    }
    
}
