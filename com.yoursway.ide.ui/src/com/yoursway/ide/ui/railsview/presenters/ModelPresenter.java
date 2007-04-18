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
import com.yoursway.rails.model.IRailsModel;
import com.yoursway.rails.model.IRailsTable;

public class ModelPresenter extends AbstractPresenter {
    
    private final IRailsModel railsModel;
    
    public ModelPresenter(IPresenterOwner owner, IRailsModel railsModel) {
        super(owner);
        this.railsModel = railsModel;
    }
    
    public boolean canEditInPlace() {
        return false;
    }
    
    public String getCaption() {
        return railsModel.getName();
    }
    
    public Object[] getChildren() {
        IRailsTable table = railsModel.getRailsProject().getSchema().findByName(railsModel.getTableName());
        Collection<Object> children = new ArrayList<Object>();
        if (table != null)
            children.addAll(table.getFields().getItems());
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
    
    public void handleDoubleClick() {
        openEditor(railsModel.getCorrespondingFile());
    }
    
    public void fillContextMenu(IContextMenuContext context) {
        // TODO Auto-generated method stub
        
    }
    
}