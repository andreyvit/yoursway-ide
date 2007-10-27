package com.yoursway.ide.ui.railsview.shit;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TreeItem;

public interface IPresentableItem {
    
    Image getImage();
    
    String getCaption();
    
    IPresentableItem[] getChildren();
    
    boolean hasChildren();
    
    void measureItem(TreeItem item, Event event);
    
    void eraseItem(TreeItem item, Event event);
    
    void paintItem(TreeItem item, Event event);
    
    int matches(String pattern);
    
}
