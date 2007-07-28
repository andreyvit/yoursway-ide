package com.yoursway.rails.discovering;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.RailsInstance;
import com.yoursway.rails.RailsInstancesManager;
import com.yoursway.ruby.ProcessResult;
import com.yoursway.ruby.RubyInstance;
import com.yoursway.rubygems.IGem;
import com.yoursway.rubygems.LocalGems;

/**
 * Inspects the given Rails instance and adds it to the registry of Rails
 * instances.
 * 
 * FIXME: move most of the code to the separate class.
 */
public class InspectRailsInstanceJob extends Job {
    private final RubyInstance ruby;
    private final String railsVersion;
    
    InspectRailsInstanceJob(RubyInstance ruby, String railsVersion) {
        super("Registering Rails " + railsVersion + " for " + ruby.toString());
        this.ruby = ruby;
        this.railsVersion = railsVersion;
    }
    
    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            SubMonitor progress = SubMonitor.convert(monitor);
            
            URL r = Activator.getDefault().getBundle().getEntry("scripts/simple_loader.rb");
            String fileName = null;
            try {
                fileName = new File(FileLocator.toFileURL(r).getFile()).getAbsolutePath();
                // without new File(...).getAbsolutePath() on windows it
                // returns path /C:/Ruby/... which ruby.exe don't understand
            } catch (IOException e) {
                // FIXME
                return Status.CANCEL_STATUS;
            }
            
            List<String> args = new ArrayList<String>();
            args.add("rails");
            args.add(railsVersion);
            ProcessResult res = ruby.runRubyScript(fileName, args, progress);
            
            // FIXME
            if (res.getExitCode() != 0) {
                System.err.println("Error: " + String.valueOf(res.getExitCode()));
                return Status.OK_STATUS;
            }
            
            IGem[] railsGemsInfo = LocalGems.parseGemsInfo(res.getOutputData());
            RailsInstance railsInstance = new RailsInstance(ruby, railsVersion, railsGemsInfo);
            RailsInstancesManager.addRailsInstance(ruby, railsVersion, railsInstance);
        } catch (Throwable t) {
            // FIXME
            System.out.println(getName());
            t.printStackTrace(System.out);
        }
        
        return Status.OK_STATUS;
    }
}