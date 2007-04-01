package com.yoursway.rails;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rubygems.IGem;
import com.yoursway.rubygems.LocalGems;
import com.yoursway.utils.Streams;

public class StartSearchAction implements IWorkbenchWindowActionDelegate {
    class RailsSearchJob extends Job {
        public RailsSearchJob(String name) {
            super(name);
        }
        
        @Override
        protected IStatus run(IProgressMonitor monitor) {
            monitor.beginTask("Searching Rails", IProgressMonitor.UNKNOWN);
            
            URL scriptUrl = Activator.getDefault().getBundle().getEntry("scripts/simple_loader.rb");
            String loaderScript;
            try {
                loaderScript = Streams.readToString(scriptUrl.openStream());
            } catch (IOException e1) {
                //FIXME
                return Status.OK_STATUS;
            }
            
            String stdout;
            try {
                //FIXME
                Process p = DebugPlugin.exec(new String[] { "/usr/local/bin/ruby", "-", "rails" }, null);
                Streams.writeString(p.getOutputStream(), loaderScript);
                p.waitFor();
                
                //FIXME: check errors
                
                stdout = Streams.readToString(p.getInputStream());
            } catch (CoreException e) {
                //FIXME
                return Status.OK_STATUS;
            } catch (InterruptedException e) {
                //FIXME
                return Status.OK_STATUS;
            } catch (IOException e) {
                //FIXME
                return Status.OK_STATUS;
            }
            
            LocalGems lg = new LocalGems();
            
            String[] versions = lg.parseGemVersions(stdout);
            
            for (String version : versions) {
                String res;
                try {
                    Process p = DebugPlugin.exec(
                            new String[] { "/usr/local/bin/ruby", "-", "rails", version }, null);
                    Streams.writeString(p.getOutputStream(), loaderScript);
                    p.waitFor();
                    
                    //FIXME: check errors
                    
                    res = Streams.readToString(p.getInputStream());
                } catch (CoreException e) {
                    return Status.OK_STATUS;
                } catch (IOException e) {
                    return Status.OK_STATUS;
                } catch (InterruptedException e) {
                    return Status.OK_STATUS;
                }
                
                Set<String> paths = new HashSet<String>();
                for (IGem g : lg.parseGemsInfo(res)) {
                    String gemDirectory = g.getDirectory();
                    for (String requirePath : g.getRequirePaths()) {
                        //FIXME: use <code>File</code>
                        paths.add(gemDirectory + "/" + requirePath);
                    }
                }
                
                RailsRuntime.addRails(new Rails(version, paths));
            }
            
            monitor.done();
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
