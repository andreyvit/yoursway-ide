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

public class ControllersFolderPresenter extends AbstractPresenter {
    
    //    private final IRailsControllersFolder controllersFolder;
    
    //    public ControllersFolderPresenter(IPresenterOwner owner, IRailsControllersFolder controllersFolder) {
    //        super(owner);
    //        //        this.controllersFolder = controllersFolder;
    //    }
    
    public ControllersFolderPresenter(IPresenterOwner owner) {
        super(owner);
        // TODO Auto-generated constructor stub
    }
    
    public boolean canEditInPlace() {
        return false;
    }
    
    public String getCaption() {
        return null;
        //        return controllersFolder.getCorrespondingFolder().getProjectRelativePath().toString();
    }
    
    public Object[] getChildren() {
        Collection<Object> children = new ArrayList<Object>();
        return children.toArray();
    }
    
    public ImageDescriptor getImage() {
        return RailsViewImages.EMPTY_ICON;
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
        // TODO Auto-generated method stub
        
    }
    
}