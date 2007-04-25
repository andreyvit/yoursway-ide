/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

import com.yoursway.ui.popup.IPopupHost;

final class PopupHostAdapter implements IPopupHost {
    private final IRenameContext context;
    
    PopupHostAdapter(IRenameContext context) {
        this.context = context;
    }
    
    public Composite getParent() {
        return context.getTree();
    }
    
    public Shell getShell() {
        return context.getWorkbenchPage().getWorkbenchWindow().getShell();
    }
    
    public IWorkbenchWindow getWorkbenchWindow() {
        return context.getWorkbenchPage().getWorkbenchWindow();
    }
}