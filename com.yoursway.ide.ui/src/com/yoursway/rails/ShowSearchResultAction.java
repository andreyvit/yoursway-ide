package com.yoursway.rails;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


public class ShowSearchResultAction implements IWorkbenchWindowActionDelegate {
    
    private IWorkbenchWindow window;
    
    public void dispose() {
    }
    
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }
    
    public void run(IAction action) {
        MessageBox mb = new MessageBox(this.window.getShell());
        
        String msg = "";
        for (Rails r : RailsRuntime.getRails())
            msg += r.getVersion() + " " + r.getPaths().toString() + "\n";
        mb.setMessage(msg);
        mb.open();
    }
    
    public void selectionChanged(IAction action, ISelection selection) {
    }
}
