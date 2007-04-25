/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;

import com.yoursway.ide.ui.railsview.presentation.AbstractPresenter;
import com.yoursway.ide.ui.railsview.presentation.IContextMenuContext;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.rails.model.IRailsProject;

public class ProjectPresenter extends AbstractPresenter {
    
    private final IRailsProject railsProject;
    
    public ProjectPresenter(IPresenterOwner owner, IRailsProject railsProject) {
        super(owner);
        this.railsProject = railsProject;
    }
    
    public boolean canEditInPlace() {
        return false;
    }
    
    public String getCaption() {
        return null;
    }
    
    public Object[] getChildren() {
        Collection<Object> children = new ArrayList<Object>();
        children.addAll(railsProject.getControllersCollection().getRootFolder().getControllersCollection()
                .getControllers());
        children.addAll(railsProject.getModelsCollection().getItems());
        return children.toArray();
    }
    
    public ImageDescriptor getImage() {
        return null;
    }
    
    public Object getParent() {
        return null;
    }
    
    public boolean hasChildren() {
        return true;
    }
    
    public void handleDoubleClick() {
    }
    
    public void fillContextMenu(IContextMenuContext context) {
    }
    
}