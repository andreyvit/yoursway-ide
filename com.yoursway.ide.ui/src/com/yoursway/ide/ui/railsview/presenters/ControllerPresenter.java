/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;

import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.ide.ui.railsview.presentation.AbstractPresenter;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.rails.model.IRailsController;
import com.yoursway.utils.RailsNamingConventions;

public class ControllerPresenter extends AbstractPresenter {
    
    private final IRailsController railsController;
    
    public ControllerPresenter(IPresenterOwner owner, IRailsController railsController) {
        super(owner);
        this.railsController = railsController;
    }
    
    public boolean canEditInPlace() {
        return false;
    }
    
    public String getCaption() {
        String[] classNameComponents = railsController.getExpectedClassName();
        return RailsNamingConventions.joinNamespaces(classNameComponents);
    }
    
    public Object[] getChildren() {
        Collection<Object> children = new ArrayList<Object>();
        children.addAll(railsController.getActionsCollection().getActions());
        children.addAll(railsController.getViewsCollection().getItems());
        return children.toArray();
    }
    
    public ImageDescriptor getImage() {
        return RailsViewImages.CONTROLLER_ICON;
    }
    
    public Object getParent() {
        return railsController.getRailsProject();
    }
    
    public boolean hasChildren() {
        return railsController.getActionsCollection().hasItems()
                || railsController.getViewsCollection().hasItems();
    }
    
    public void handleDoubleClick() {
        openEditor(railsController.getFile());
    }
    
}