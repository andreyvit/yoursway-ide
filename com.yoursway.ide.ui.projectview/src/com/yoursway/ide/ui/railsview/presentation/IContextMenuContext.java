package com.yoursway.ide.ui.railsview.presentation;

import org.eclipse.jface.action.IMenuManager;

public interface IContextMenuContext extends IProvidesTreeItem {
    
    IMenuManager getMenuManager();
    
}
