/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters.controller;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Event;

import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.ide.ui.railsview.presentation.AbstractPresenter;
import com.yoursway.ide.ui.railsview.presentation.IContextMenuContext;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.ide.ui.railsview.presentation.IProvidesTreeItem;
import com.yoursway.ide.ui.railsview.presenters.RenameContextAdapter;
import com.yoursway.ide.ui.railsview.presenters.RenameMode;
import com.yoursway.rails.models.controller.RailsController;
import com.yoursway.utils.RailsNamingConventions;
import com.yoursway.utils.StringUtils;

public class ControllerPresenter extends AbstractPresenter {
    
    private final static class Context extends RenameContextAdapter {
        
        private final RailsController railsController;
        
        public Context(IPresenterOwner presenterOwner, IContextMenuContext contextMenuContext,
                RailsController railsController) {
            super(presenterOwner, contextMenuContext);
            this.railsController = railsController;
        }
        
        public RailsController getRailsController() {
            return railsController;
        }
        
        public String getInitialValue() {
            return StringUtils.join(railsController.getPathComponents(), "/");
        }
        
        public boolean isValidValue(String value) {
            return true;
        }
        
        public void setValue(String value) {
        }
        
    }
    
    public final static class RenameAction extends Action {
        private final Context renameContext;
        private RenameMode renameMode;
        
        private RenameAction(Context renameContext) {
            super("Rename " + renameContext.getRailsController().getDisplayName());
            this.renameContext = renameContext;
        }
        
        @Override
        public void runWithEvent(Event event) {
            renameMode = new RenameMode(renameContext);
            renameMode.enterMode();
        }
    }
    
    private final RailsController railsController;
    
    public ControllerPresenter(IPresenterOwner owner, RailsController railsController) {
        super(owner);
        this.railsController = railsController;
    }
    
    public boolean canEditInPlace() {
        return false;
    }
    
    public String getCaption() {
        String[] classNameComponents = railsController.getFullClassName();
        return "  " + RailsNamingConventions.joinNamespaces(classNameComponents);
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
        //        context.getMenuManager().add(new RenameAction(new Context(getOwner(), context, railsController)));
        context.getMenuManager().add(new DeleteFileAction(railsController.getFile()));
    }
}
