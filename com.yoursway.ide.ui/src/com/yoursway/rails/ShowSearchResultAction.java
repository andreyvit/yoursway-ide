package com.yoursway.rails;

import java.util.List;

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
        List<Rails> rails = RailsRuntime.getRails();
        
        String railsInfo = "Rails installations:\n";
        
        for (Rails r : rails) {
            railsInfo += "Rails " + r.getVersion() + " (" + r.getRuby().getName() + ")\n";
            for (String s : r.getPaths()) {
                railsInfo += "  [" + s + "]\n";
            }
            railsInfo += "\n";
        }
        
        MessageBox mb = new MessageBox(window.getShell());
        mb.setMessage(railsInfo);
        mb.open();
    }
    
    public void selectionChanged(IAction action, ISelection selection) {
    }
}
