/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;

import com.yoursway.common.SegmentedName;
import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.ide.ui.railsview.presentation.AbstractPresenter;
import com.yoursway.ide.ui.railsview.presentation.IContextMenuContext;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.ide.ui.railsview.presentation.IProvidesTreeItem;
import com.yoursway.rails.core.controllers.RailsController;
import com.yoursway.utils.RailsNamingConventions;

public class ControllerPresenter extends AbstractPresenter {
    
    private final RailsController railsController;
    
    public ControllerPresenter(IPresenterOwner owner, RailsController railsController) {
        super(owner);
        this.railsController = railsController;
    }
    
    public boolean canEditInPlace() {
        return false;
    }
    
    public String getCaption() {
        SegmentedName classNameComponents = railsController.getFullClassName();
        return RailsNamingConventions.joinNamespaces(classNameComponents);
        //        return railsController.getFullClassName().getTrailingSegment();
    }
    
    public Object[] getChildren() {
        Collection<Object> children = new ArrayList<Object>();
        //        children.addAll(railsController.getActionsCollection().getActions());
        //        children.addAll(railsController.getViewsCollection().getItems());
        return children.toArray();
    }
    
    public ImageDescriptor getImage() {
        return RailsViewImages.CONTROLLER_ICON;
    }
    
    public Object getParent() {
        return railsController.getRailsProject();
    }
    
    public boolean hasChildren() {
        return false;
        //        return railsController.getActionsCollection().hasItems()
        //                || railsController.getViewsCollection().hasItems();
    }
    
    public void handleDoubleClick(IProvidesTreeItem context) {
        openEditor(railsController.getFile());
    }
    
    public void fillContextMenu(final IContextMenuContext context) {
        context.getMenuManager().add(new DeleteFileAction(railsController.getFile()));
    }
}
