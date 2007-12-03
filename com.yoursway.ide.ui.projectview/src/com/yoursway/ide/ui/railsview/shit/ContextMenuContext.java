package com.yoursway.ide.ui.railsview.shit;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

public class ContextMenuContext implements IContextMenuContext {
    
    private final IMenuManager menuManager;
    private final IStructuredSelection selection;
    
    public ContextMenuContext(IMenuManager menuManager, IStructuredSelection selection) {
        this.menuManager = menuManager;
        this.selection = selection;
    }
    
    public IStructuredSelection getContext() {
        return selection;
    }
    
    public IMenuManager getMenuManager() {
        return menuManager;
    }
    
}
