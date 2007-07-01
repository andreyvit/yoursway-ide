/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;

import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.ide.ui.railsview.presentation.AbstractPresenter;
import com.yoursway.ide.ui.railsview.presentation.IContextMenuContext;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.ide.ui.railsview.presentation.IProvidesTreeItem;
import com.yoursway.rails.core.models.RailsModel;

public class ModelPresenter extends AbstractPresenter {
    
    private final RailsModel railsModel;
    
    public ModelPresenter(IPresenterOwner owner, RailsModel railsModel) {
        super(owner);
        this.railsModel = railsModel;
    }
    
    public boolean canEditInPlace() {
        return false;
    }
    
    public String getCaption() {
        return railsModel.getDisplayName();
    }
    
    public Object[] getChildren() {
        Collection<Object> children = new ArrayList<Object>();
        //        IRailsTable table = railsModel.getRailsProject().getSchema().findByName(railsModel.getTableName());
        //        if (table != null)
        //            children.addAll(table.getFields().getItems());
        return children.toArray();
    }
    
    public ImageDescriptor getImage() {
        return RailsViewImages.MODEL_ICON;
    }
    
    public Object getParent() {
        return railsModel.getRailsProject();
    }
    
    public boolean hasChildren() {
        return getChildren().length > 0;
    }
    
    public void handleDoubleClick(IProvidesTreeItem context) {
        openEditor(railsModel.getFile());
    }
    
    public void fillContextMenu(IContextMenuContext context) {
        context.getMenuManager().add(new DeleteFileAction(railsModel.getFile()));
    }
    
}