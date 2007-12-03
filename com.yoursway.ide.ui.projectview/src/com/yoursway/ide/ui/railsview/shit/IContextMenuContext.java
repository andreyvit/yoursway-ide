package com.yoursway.ide.ui.railsview.shit;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

public interface IContextMenuContext {

    IMenuManager getMenuManager();
    
    IStructuredSelection getContext();
    
}
