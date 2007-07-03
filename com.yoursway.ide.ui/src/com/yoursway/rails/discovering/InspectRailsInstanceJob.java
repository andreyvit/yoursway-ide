package com.yoursway.rails.discovering;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterConfig;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.RailsInstance;
import com.yoursway.rails.RailsInstancesManager;
import com.yoursway.ruby.RubyInstance;
import com.yoursway.rubygems.IGem;
import com.yoursway.rubygems.LocalGems;
import com.yoursway.utils.InterpreterRunnerUtil;

/**
 * Inspects the given Rails instance and adds it to the registry of Rails
 * instances.
 * 
 * FIXME: move most of the code to the separate class.
 */
public class InspectRailsInstanceJob extends Job {
    private final IInterpreterInstall rubyInterpreter;
    private final String railsVersion;
    
    InspectRailsInstanceJob(IInterpreterInstall rubyInterpreter, String railsVersion) {
        super("Registering Rails " + railsVersion + " for " + rubyInterpreter.getName());
        this.rubyInterpreter = rubyInterpreter;
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
            
            InterpreterConfig config = new InterpreterConfig(new File(fileName));
            config.addScriptArgs(new String[] { "rails", railsVersion });
            
            ILaunch launch = null;
            try {
                launch = InterpreterRunnerUtil.run(rubyInterpreter, config, progress
                        .newChild(IProgressMonitor.UNKNOWN));
            } catch (CoreException c) {
                // FIXME
            }
            
            if (launch == null)
                return Status.CANCEL_STATUS;
            
            IProcess[] launchProcesses = launch.getProcesses();
            assert launchProcesses.length == 1 : "Ruby script launch is expected to have single process";
            
            IProcess launchProcess = launchProcesses[0];
            
            System.out.println(getName() + " exit value: "
                    + String.valueOf(InterpreterRunnerUtil.getFinishedProcessExitValue(launchProcess)));
            
            // FIXME: proper error handling
            String errorMsg = RailsInstancesManager.checkFinishedProcess(launchProcess, getName());
            if (errorMsg != null) {
                System.out.println(getName() + " " + errorMsg);
                return Status.OK_STATUS;
            }
            
            if (InterpreterRunnerUtil.getFinishedProcessExitValue(launchProcess) != 0)
                return Status.OK_STATUS;
            
            String railsInfo = launchProcess.getStreamsProxy().getOutputStreamMonitor().getContents();
            System.out.println(getName() + " " + railsInfo);
            
            IGem[] railsGemsInfo = LocalGems.parseGemsInfo(railsInfo);
            
            System.out.println(getName());
            for (IGem railsGem : railsGemsInfo) {
                System.out.println("  " + railsGem.getName() + " " + railsGem.getVersion());
            }
            
            Set<String> paths = new HashSet<String>();
            
            RubyInstance rubyInstance = RubyInstance.adapt(rubyInterpreter);
            RailsInstance railsInstance = new RailsInstance(rubyInstance, railsVersion, railsGemsInfo);
            RailsInstancesManager.addRailsInstance(rubyInterpreter, railsVersion, railsInstance);
        } catch (Throwable t) {
            System.out.println(getName());
            t.printStackTrace(System.out);
        }
        
        return Status.OK_STATUS;
    }
}