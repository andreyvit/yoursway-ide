package com.yoursway.ide.ui.railsview.presentation;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.TreeItem;

public interface IContextMenuContext {
    
    IMenuManager getMenuManager();
    
    TreeItem getTreeItem();
    
}
