package com.yoursway.rails;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class StartSearchAction implements IWorkbenchWindowActionDelegate {
    class RailsSearchJob extends Job {
        public RailsSearchJob(String name) {
            super(name);
        }
        
        @Override
        protected IStatus run(IProgressMonitor monitor) {
            RailsRuntime.runSearchRails();
            return Status.OK_STATUS;
        }
    }
    
    public void dispose() {
    }
    
    public void init(IWorkbenchWindow window) {
    }
    
    public void run(IAction action) {
        Job railsSearchJob = new RailsSearchJob("RailsSearchJob");
        railsSearchJob.schedule();
    }
    
    public void selectionChanged(IAction action, ISelection selection) {
    }
}
