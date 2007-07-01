/**
 * 
 */
package com.yoursway.rails.launching;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.dltk.launching.ScriptLaunchConfigurationConstants;
import org.eclipse.dltk.ruby.core.RubyNature;
import org.eclipse.dltk.ruby.launching.RubyLaunchConfigurationConstants;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.ruby.RubyInstance;
import com.yoursway.ruby.RubyToolUtils;
import com.yoursway.utils.InterpreterRunnerUtil;
import com.yoursway.utils.ServerUtils;
import com.yoursway.utils.ServerUtils.NoFreePortFound;

class ProjectLaunching implements IProjectLaunching {
    
    /**
     * 
     */
    private final RailsServersModel model;
    private LaunchState state = LaunchState.NOT_RUNNING;
    private int port;
    
    private int pidToKill;
    
    private final class StartServerJob extends Job {
        
        private StartServerJob() {
            super("Launching " + railsProject.getProject().getName());
        }
        
        @Override
        protected IStatus run(IProgressMonitor monitor) {
            IFile serverFile = railsProject.getProject().getFile("script/server");
            ILaunchManager lm = DebugPlugin.getDefault().getLaunchManager();
            ILaunchConfigurationType rubyLaunchConfigurationType = lm
                    .getLaunchConfigurationType(RubyLaunchConfigurationConstants.ID_RUBY_SCRIPT);
            
            removeStaleLaunchConfigurations(serverFile, lm, rubyLaunchConfigurationType);
            
            final int startingPort = RailsLaunchingConstants.DEFAULT_PORT;
            int port;
            try {
                port = ServerUtils.findFreePort(startingPort, RailsLaunchingConstants.NUMBER_OF_PORTS_TO_TRY);
            } catch (NoFreePortFound e) {
                Activator.reportException(e, "Launching failed");
                return Status.CANCEL_STATUS;
            }
            setPort(port);
            
            List<String> arguments = new ArrayList<String>();
            arguments.add("webrick");
            arguments.add("--port=" + port);
            
            final ILaunchConfiguration configToLaunch;
            ILaunchConfigurationWorkingCopy wc = null;
            try {
                ILaunchConfigurationType configType = rubyLaunchConfigurationType;
                wc = configType.newInstance(null, lm.generateUniqueLaunchConfigurationNameFrom(serverFile
                        .getName()));
                wc.setAttribute(ScriptLaunchConfigurationConstants.ATTR_SCRIPT_NATURE, RubyNature.NATURE_ID);
                wc.setAttribute(ScriptLaunchConfigurationConstants.ATTR_PROJECT_NAME, serverFile.getProject()
                        .getName());
                wc.setAttribute(ScriptLaunchConfigurationConstants.ATTR_MAIN_SCRIPT_NAME, serverFile
                        .getProjectRelativePath().toPortableString());
                wc.setAttribute(ScriptLaunchConfigurationConstants.ATTR_SCRIPT_ARGUMENTS,
                        InterpreterRunnerUtil.convertCommandLineToString(arguments));
                wc.setAttribute(DebugPlugin.ATTR_PROCESS_FACTORY_ID, PublicMorozovProcessFactory.ID);
                wc.setAttribute(RailsLaunchingConstants.ATTR_YOURSWAY_CREATED, "true");
                wc.setMappedResources(new IResource[] { serverFile.getProject() });
                configToLaunch = wc.doSave();
            } catch (CoreException exception) {
                Activator.unexpectedError(exception, "Cannot create launch configuration for " + serverFile);
                return Status.CANCEL_STATUS;
            }
            
            try {
                launch = configToLaunch.launch("run", monitor);
            } catch (CoreException e) {
                Activator.reportException(e, "Launching of " + railsProject.getProject().getName());
                return Status.CANCEL_STATUS;
            }
            
            IProcess[] processes = launch.getProcesses();
            pidToKill = 0; // by default don't kill anyone
            if (processes.length != 1)
                Activator.unexpectedError("Unexpected number of processes, cannot extract pid: "
                        + processes.length);
            else {
                IProcess runtimeProcess = processes[0];
                if (runtimeProcess instanceof PublicMorozovProcess) {
                    pidToKill = ((PublicMorozovProcess) runtimeProcess).getPid();
                    System.out.println("Launched Rails server PID: " + pidToKill);
                }
            }
            
            // TODO: add time limit (maybe)
            while (!launch.isTerminated() && ServerUtils.isPortAvailable(port)) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // hmmm
                    Activator.unexpectedError(e);
                }
            }
            
            if (!ServerUtils.isPortAvailable(port))
                setStarted();
            else {
                setFailed();
                // TODO: more help for user on server failure (pop up console?)
            }
            
            return Status.OK_STATUS;
        }
        
        private void removeStaleLaunchConfigurations(IFile serverFile, ILaunchManager lm,
                ILaunchConfigurationType rubyLaunchConfigurationType) {
            ILaunchConfiguration[] configs;
            try {
                configs = lm.getLaunchConfigurations(rubyLaunchConfigurationType);
            } catch (CoreException e1) {
                Activator.unexpectedError(e1);
                configs = new ILaunchConfiguration[0];
            }
            for (int i = 0; i < configs.length; i++) {
                ILaunchConfiguration config = configs[i];
                try {
                    if (config
                            .getAttribute(ScriptLaunchConfigurationConstants.ATTR_MAIN_SCRIPT_NAME, "").equals(serverFile.getProjectRelativePath().toPortableString())) { //$NON-NLS-1$
                        if (config.getAttribute(RailsLaunchingConstants.ATTR_YOURSWAY_CREATED, "").length() > 0)
                            config.delete();
                    }
                } catch (CoreException e) {
                    Activator.unexpectedError(e);
                }
            }
        }
    }
    
    private final class StopServerJob extends Job {
        private StopServerJob() {
            super("Stopping " + railsProject.getProject().getName());
        }
        
        @Override
        protected IStatus run(IProgressMonitor monitor) {
            final RubyInstance ruby = getRubyInstanceToRunTools();
            if (pidToKill != 0) {
                System.out.println("Sending SIGINT to process " + pidToKill + "...");
                RubyToolUtils.sendInterruptSignal(ruby, pidToKill, null);
                waitForTermination(monitor, 500);
                if (launch.isTerminated()) {
                    System.out.println("Server has terminated.");
                } else {
                    System.out.println("Timeout. Sending SIGKILL to process " + pidToKill + "...");
                    RubyToolUtils.sendKillSignal(ruby, pidToKill, null);
                    waitForTermination(monitor, 500);
                    if (launch.isTerminated()) {
                        System.out.println("Server has terminated.");
                    }
                }
            }
            if (!launch.isTerminated())
                try {
                    launch.terminate();
                } catch (DebugException e) {
                    Activator.reportException(e, "Cannot terminate Rails server");
                }
            setStopped();
            return Status.OK_STATUS;
        }
    }
    
    private void waitForTermination(IProgressMonitor monitor, int timeout) {
        int period = 50;
        int periods = timeout / period;
        while (!launch.isTerminated())
            try {
                monitor.worked(1);
                Thread.sleep(period);
            } catch (InterruptedException e) {
            }
    }
    
    private final RailsProject railsProject;
    protected ILaunch launch;
    
    public ProjectLaunching(RailsServersModel railsServersModel, RailsProject railsProject) {
        model = railsServersModel;
        this.railsProject = railsProject;
    }
    
    public RailsProject getRailsProject() {
        return railsProject;
    }
    
    public synchronized void startDefaultServer() {
        switch (state) {
        case NOT_RUNNING:
            break; // okay
        case LAUNCHING:
        case RUNNING:
            return; // do nothing
        case STOPPING:
            return; // XXX what can we do? don't want to throw an exception
        default:
            assert false; // unreachable
        }
        setState(LaunchState.LAUNCHING);
        Job job = new StartServerJob();
        job.schedule();
    }
    
    public synchronized void stopServer() {
        switch (state) {
        case NOT_RUNNING:
        case STOPPING:
            return;
        case LAUNCHING:
            cancelLaunching();
            return;
        case RUNNING:
            break;
        default:
            assert false; // unreachable
        }
        setState(LaunchState.STOPPING);
        Job job = new StopServerJob();
        job.schedule();
    }
    
    synchronized void setStarted() {
        setState(LaunchState.RUNNING);
    }
    
    synchronized void setFailed() {
        port = 0;
        setState(LaunchState.FAILED);
    }
    
    synchronized void setStopped() {
        port = 0;
        setState(LaunchState.NOT_RUNNING);
    }
    
    private void cancelLaunching() {
        setState(LaunchState.STOPPING);
    }
    
    private void setState(LaunchState state) {
        this.state = state;
        model.fireProjectStateChanged(this);
    }
    
    public LaunchState getState() {
        return state;
    }
    
    public synchronized int getPortNumber() throws PortNumberNotAvailable {
        if (port == 0)
            throw new PortNumberNotAvailable();
        return port;
    }
    
    void setPort(int port) {
        this.port = port;
        model.fireProjectStateChanged(this);
    }
    
    public RubyInstance getRubyInstanceToRunTools() {
        Collection<? extends RubyInstance> installations = RubyInstance.getRubyInstances();
        if (installations.isEmpty())
            return null;
        else
            return installations.iterator().next();
    }
    
}