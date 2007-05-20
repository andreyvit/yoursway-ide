/**
 * 
 */
package com.yoursway.rails.models.launch;

import java.util.ArrayList;
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
import org.eclipse.dltk.launching.IDLTKLaunchConfigurationConstants;
import org.eclipse.dltk.ruby.core.RubyNature;
import org.eclipse.dltk.ruby.launching.IRubyLaunchConfigurationConstants;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.model.IRailsProject;
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
    
    private final class StartServerJob extends Job {
        
        private StartServerJob() {
            super("Launching " + railsProject.getProject().getName());
        }
        
        @Override
        protected IStatus run(IProgressMonitor monitor) {
            IFile serverFile = railsProject.getProject().getFile("script/server");
            ILaunchManager lm = DebugPlugin.getDefault().getLaunchManager();
            ILaunchConfigurationType rubyLaunchConfigurationType = lm
                    .getLaunchConfigurationType(IRubyLaunchConfigurationConstants.ID_RUBY_SCRIPT);
            
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
            arguments.add("--port=" + port);
            
            final ILaunchConfiguration configToLaunch;
            ILaunchConfigurationWorkingCopy wc = null;
            try {
                ILaunchConfigurationType configType = rubyLaunchConfigurationType;
                wc = configType.newInstance(null, lm.generateUniqueLaunchConfigurationNameFrom(serverFile
                        .getName()));
                wc.setAttribute(IDLTKLaunchConfigurationConstants.ATTR_NATURE, RubyNature.NATURE_ID);
                wc.setAttribute(IDLTKLaunchConfigurationConstants.ATTR_PROJECT_NAME, serverFile.getProject()
                        .getName());
                wc.setAttribute(IDLTKLaunchConfigurationConstants.ATTR_MAIN_SCRIPT_NAME, serverFile
                        .getProjectRelativePath().toPortableString());
                wc.setAttribute(IDLTKLaunchConfigurationConstants.ATTR_SCRIPT_ARGUMENTS,
                        InterpreterRunnerUtil.convertCommandLineToString(arguments));
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
            
            while (!launch.isTerminated() && ServerUtils.isPortAvailable(port)) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // hmmm
                    Activator.unexpectedError(e);
                }
            }
            
            if (!ServerUtils.isPortAvailable(port))
                setFailed();
            else
                setStarted();
            
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
                            .getAttribute(IDLTKLaunchConfigurationConstants.ATTR_MAIN_SCRIPT_NAME, "").equals(serverFile.getProjectRelativePath().toPortableString())) { //$NON-NLS-1$
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
            for (IProcess process : launch.getProcesses()) {
                
            }
            try {
                launch.terminate();
            } catch (DebugException e) {
                Activator.reportException(e, "Cannot terminate Rails server");
            }
            setStopped();
            return Status.OK_STATUS;
        }
    }
    
    private final IRailsProject railsProject;
    protected ILaunch launch;
    
    public ProjectLaunching(RailsServersModel railsServersModel, IRailsProject railsProject) {
        model = railsServersModel;
        this.railsProject = railsProject;
    }
    
    public IRailsProject getRailsProject() {
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
    
}