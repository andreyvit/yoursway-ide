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
import com.yoursway.rails.model.IRailsField;

public class FieldPresenter extends AbstractPresenter {
    
    private final IRailsField railsField;
    
    public FieldPresenter(IPresenterOwner owner, IRailsField railsField) {
        super(owner);
        this.railsField = railsField;
    }
    
    public boolean canEditInPlace() {
        return false;
    }
    
    public String getCaption() {
        return railsField.getName() + " (" + railsField.getType() + ")";
    }
    
    public Object[] getChildren() {
        Collection<Object> children = new ArrayList<Object>();
        return children.toArray();
    }
    
    public ImageDescriptor getImage() {
        return RailsViewImages.FIELD_ICON;
    }
    
    public Object getParent() {
        return null;
    }
    
    public boolean hasChildren() {
        return false;
    }
    
    public void handleDoubleClick(IProvidesTreeItem context) {
    }
    
    public void fillContextMenu(IContextMenuContext context) {
        
    }
    
}
