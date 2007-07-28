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
import com.yoursway.ruby.ProcessResult;
import com.yoursway.ruby.RubyInstance;
import com.yoursway.rubygems.LocalGems;
import com.yoursway.utils.SystemUtilities;

/**
 * Searches for all Rails instances in single Ruby installation, and triggers
 * the getting information about each installed Rails instance.
 * 
 * FIXME: move most of the code to the separate class.
 * 
 * FIXME: outer try/catch is a workaround: sometimes ruby1.9 process reports
 * exit code 0 instead of 3.
 */
public class DiscoverRailsInstancesInsideGivenRubyJob extends Job {
    private final RubyInstance ruby;
    
    DiscoverRailsInstancesInsideGivenRubyJob(RubyInstance rubyInterpreter) {
        super("Searching Rails in " + rubyInterpreter.toString());
        this.ruby = rubyInterpreter;
    }
    
    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            SubMonitor progress = SubMonitor.convert(monitor);
            
            URL r = Activator.getDefault().getBundle().getEntry("scripts/simple_loader.rb");
            File helperScript = null;
            try {
                helperScript = SystemUtilities.getFileSystemPathFromLocalURL(FileLocator.toFileURL(r));
            } catch (IOException e) {
                // FIXME
                return Status.CANCEL_STATUS;
            }
            
            List<String> args = new ArrayList<String>();
            args.add("rails");
            ProcessResult res = ruby.runRubyScript(helperScript.getAbsolutePath(), args, progress);
            
            if (res.getExitCode() != 0) {
                //FIXME
                System.err.println("Exit code:" + String.valueOf(res.getExitCode()) + "\n");
                return Status.OK_STATUS;
            }
            
            for (String railsGemVersion : LocalGems.parseGemVersions(res.getOutputData()))
                new InspectRailsInstanceJob(ruby, railsGemVersion).schedule();
        } catch (Throwable t) {
            // FIXME
            System.out.println(getName());
            t.printStackTrace(System.out);
        }
        
        return Status.OK_STATUS;
    }
}
