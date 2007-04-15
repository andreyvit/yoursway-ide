package com.yoursway.rails;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.InterpreterRunnerConfiguration;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.ruby.core.RubyNature;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rubygems.IGem;
import com.yoursway.rubygems.LocalGems;

public class RailsRuntime {
    
    private static Map<String, Map<String, Rails>> rails = new HashMap<String, Map<String, Rails>>();
    
    public synchronized static List<Rails> getRails() {
        ArrayList<Rails> railsList = new ArrayList<Rails>();
        for (Map<String, Rails> rs : rails.values())
            railsList.addAll(rs.values());
        return railsList;
    }
    
    private synchronized static void addRails(IInterpreterInstall ruby, String railsVersion,
            Rails railsInstance) {
        if (!rails.containsKey(ruby.getId()))
            rails.put(ruby.getId(), new HashMap<String, Rails>());
        
        System.out.println("Adding/updating rails instance: " + ruby.getName() + " rails " + railsVersion);
        
        rails.get(ruby.getId()).put(railsVersion, railsInstance);
    }
    
    public synchronized static void removeRails(IInterpreterInstall ruby) {
        System.out.println("Removing all rails instances for " + ruby.getName());
        rails.remove(ruby.getId());
    }
    
    /**
     * Inspects the given Rails instance and adds it to the registry of Rails
     * instances.
     * 
     * FIXME: move most of the code to the separate class.
     */
    private static class RailsRegisterJob extends Job {
        private final IInterpreterInstall rubyInterpreter;
        private final String railsVersion;
        
        RailsRegisterJob(IInterpreterInstall rubyInterpreter, String railsVersion) {
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
                    fileName = FileLocator.toFileURL(r).getFile();
                } catch (IOException e) {
                    //FIXME
                    return Status.CANCEL_STATUS;
                }
                
                InterpreterRunnerConfiguration config = new InterpreterRunnerConfiguration(fileName);
                config.setProgramArguments(new String[] { "rails", railsVersion });
                
                ILaunch launch = null;
                try {
                    launch = InterpreterRunnerUtil.run(rubyInterpreter, config, progress
                            .newChild(IProgressMonitor.UNKNOWN));
                } catch (CoreException c) {
                    //FIXME
                }
                if (launch == null)
                    return Status.CANCEL_STATUS;
                
                IProcess[] launchProcesses = launch.getProcesses();
                assert launchProcesses.length == 1 : "Ruby script launch is expected to have single process";
                
                IProcess launchProcess = launchProcesses[0];
                
                System.out.println(getName() + " exit value: "
                        + String.valueOf(getFinishedProcessExitValue(launchProcess)));
                
                //FIXME: proper error handling
                String errorMsg = checkFinishedProcess(launchProcess, getName());
                if (errorMsg != null) {
                    System.out.println(getName() + " " + errorMsg);
                    return Status.OK_STATUS;
                }
                
                if (RailsRuntime.getFinishedProcessExitValue(launchProcess) != 0)
                    return Status.OK_STATUS;
                
                String railsInfo = launchProcess.getStreamsProxy().getOutputStreamMonitor().getContents();
                System.out.println(getName() + " " + railsInfo);
                
                IGem[] railsGemsInfo = LocalGems.parseGemsInfo(railsInfo);
                
                System.out.println(getName());
                for (IGem railsGem : railsGemsInfo) {
                    System.out.println("  " + railsGem.getName() + " " + railsGem.getVersion());
                }
                
                Set<String> paths = new HashSet<String>();
                for (IGem railsGem : railsGemsInfo)
                    for (String requirePath : railsGem.getRequirePaths()) {
                        Path p = new Path(railsGem.getDirectory());
                        p.append(requirePath);
                        paths.add(p.toString());
                    }
                
                Rails rails = new Rails(rubyInterpreter, railsVersion, paths);
                addRails(rubyInterpreter, railsVersion, rails);
            } catch (Throwable t) {
                System.out.println(getName());
                t.printStackTrace(System.out);
            }
            
            return Status.OK_STATUS;
        }
    }
    
    /**
     * Searches for all Rails instances in single Ruby installation, and
     * triggers the getting information about each installed Rails instance.
     * 
     * FIXME: move most of the code to the separate class.
     * 
     * FIXME: outer try/catch is a workaround: sometimes ruby1.9 process reports
     * exit code 0 instead of 3.
     */
    private static class RailsSearchJob extends Job {
        private final IInterpreterInstall rubyInterpreter;
        
        RailsSearchJob(IInterpreterInstall rubyInterpreter) {
            super("Searching Rails in " + rubyInterpreter.getName());
            this.rubyInterpreter = rubyInterpreter;
        }
        
        @Override
        protected IStatus run(IProgressMonitor monitor) {
            try {
                SubMonitor progress = SubMonitor.convert(monitor);
                
                URL r = Activator.getDefault().getBundle().getEntry("scripts/simple_loader.rb");
                String fileName = null;
                try {
                    fileName = FileLocator.toFileURL(r).getFile();
                } catch (IOException e) {
                    //FIXME
                    return Status.CANCEL_STATUS;
                }
                
                InterpreterRunnerConfiguration config = new InterpreterRunnerConfiguration(fileName);
                config.setProgramArguments(new String[] { "rails" });
                
                ILaunch launch = null;
                try {
                    launch = InterpreterRunnerUtil.run(rubyInterpreter, config, progress
                            .newChild(IProgressMonitor.UNKNOWN));
                } catch (CoreException c) {
                    //FIXME
                }
                
                if (launch == null)
                    return Status.CANCEL_STATUS;
                
                IProcess[] launchProcesses = launch.getProcesses();
                assert launchProcesses.length == 1 : "Ruby script launch is expected to have single process";
                
                IProcess launchProcess = launchProcesses[0];
                
                System.out.println(getName() + " exit value: "
                        + String.valueOf(getFinishedProcessExitValue(launchProcess)));
                
                //FIXME: proper error hadling
                String errorMsg = checkFinishedProcess(launchProcess, getName());
                if (errorMsg != null) {
                    System.out.println(getName() + " " + errorMsg);
                    return Status.OK_STATUS;
                }
                
                String gemsInfo = launchProcess.getStreamsProxy().getOutputStreamMonitor().getContents();
                System.out.println(getName() + " " + gemsInfo);
                
                String[] installedRailsGemsVersions = LocalGems.parseGemVersions(gemsInfo);
                
                System.out.println(getName());
                for (String installedRailsGemVersion : installedRailsGemsVersions)
                    System.out.println(installedRailsGemVersion);
                
                for (String installedRailsGemVersion : installedRailsGemsVersions)
                    new RailsRegisterJob(rubyInterpreter, installedRailsGemVersion).schedule();
                
            } catch (Throwable t) {
                System.out.println(getName());
                t.printStackTrace(System.out);
            }
            
            return Status.OK_STATUS;
        }
    }
    
    /**
     * Starts searching Rails instances in the background.
     * 
     * @param rubyInterpreter
     *            Ruby interpreter where Rails instances will be searched in.
     */
    public static void runSearchRails(IInterpreterInstall rubyInterpreter) {
        RailsSearchJob job = new RailsSearchJob(rubyInterpreter);
        job.schedule();
    }
    
    /**
     * Starts searching Rails instances in all available Ruby instances.
     */
    public static void runSearchRails() {
        IInterpreterInstallType[] rubyInterpreters = ScriptRuntime
                .getInterpreterInstallTypes(RubyNature.NATURE_ID);
        assert rubyInterpreters.length == 1 : "Only one IInterpreterInstallType is expected for Ruby nature";
        
        for (IInterpreterInstall rubyInterpreter : rubyInterpreters[0].getInterpreterInstalls())
            runSearchRails(rubyInterpreter);
    }
    
    /**
     * Returns the exit value from the finished process.
     * 
     * @see IProcess#getExitValue()
     * @param launchProcess
     *            process to get exit value
     * @return process exit value
     */
    private static int getFinishedProcessExitValue(IProcess launchProcess) {
        try {
            return launchProcess.getExitValue();
        } catch (DebugException e) {
            throw new AssertionError("DebugError won't be thrown on finished process");
        }
    }
    
    //FIXME: ugly
    private static String checkFinishedProcess(IProcess launchProcess, String processLabel) {
        int exitValue = RailsRuntime.getFinishedProcessExitValue(launchProcess);
        switch (exitValue) {
        case 0:
            return null;
        case 1:
            return "Unable to retrieve gem info (" + processLabel + "): no gem passed to the script.";
        case 2:
            return "Unable to retrieve gem info (" + processLabel + "): gem is not found";
        case 3:
            return "Unable to retrieve gem info (" + processLabel + "): rubygems not found";
        default:
            return "Runtime error during fetching gem information (" + processLabel + "):\n"
                    + launchProcess.getStreamsProxy().getErrorStreamMonitor().getContents();
        }
    }
}
