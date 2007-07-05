package com.yoursway.ide.ui.railsview.presentation;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TreeItem;

public interface IElementPresenter {
    
    ImageDescriptor getImage();
    
    String getCaption();
    
    Object[] getChildren();
    
    boolean hasChildren();
    
    Object getParent();
    
    void handleDoubleClick(IProvidesTreeItem context);
    
    boolean canEditInPlace();
    
    void fillContextMenu(IContextMenuContext context);
    
    void measureItem(TreeItem item, Object element, Event event);
    
    void eraseItem(TreeItem item, Object element, Event event);
    
    void paintItem(TreeItem item, Object element, Event event);
    
}
