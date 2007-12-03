package com.yoursway.ide.ui.railsview.shit;

import java.util.Collection;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TreeItem;

public interface IPresentableItem {
    
    Image getImage();
    
    String getCaption();
    
    Collection<IPresentableItem> getChildren();
    
    boolean hasChildren();
    
    void measureItem(TreeItem item, Event event);
    
    void eraseItem(TreeItem item, Event event);
    
    void paintItem(TreeItem item, Event event);
    
    int matches(String pattern);
    
//    void fillContextMenu(IContextMenuContext menuManager);
    
//    Collection<IAction> getQuickClickActions(); 
    // TODO: 
    // I think that we should add not more than one button on the right side of a line
    // Also I think that added buttons should not be very different in order not to 
    // overload user's eye
    
}
