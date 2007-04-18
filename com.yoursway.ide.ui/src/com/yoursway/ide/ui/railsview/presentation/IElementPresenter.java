package com.yoursway.ide.ui.railsview.presentation;

import org.eclipse.jface.resource.ImageDescriptor;

public interface IElementPresenter {
    
    ImageDescriptor getImage();
    
    String getCaption();
    
    Object[] getChildren();
    
    boolean hasChildren();
    
    Object getParent();
    
    void handleDoubleClick();
    
    boolean canEditInPlace();
    
    void fillContextMenu(IContextMenuContext context);
    
}
