package com.yoursway.ide.projects.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.rails.core.projects.RailsProject;

public class ProjectEditorInput implements IEditorInput {
    
    private final RailsProject railsProject;
    
    public ProjectEditorInput(RailsProject project) {
        this.railsProject = project;
    }
    
    public RailsProject getRailsProject() {
        return railsProject;
    }
    
    public boolean exists() {
        return true;
    }
    
    public ImageDescriptor getImageDescriptor() {
        return RailsViewImages.CONTROLLER_ICON;
    }
    
    public String getName() {
        return railsProject.getProject().getName();
    }
    
    public IPersistableElement getPersistable() {
        return null;
    }
    
    public String getToolTipText() {
        return getName();
    }
    
    public Object getAdapter(Class adapter) {
        return null;
    }
    
}
