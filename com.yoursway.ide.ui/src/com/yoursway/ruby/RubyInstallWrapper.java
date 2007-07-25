package com.yoursway.ruby;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.InterpreterConfig;

import com.yoursway.common.StringUtils;
import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.discovering.RubyAndRailsDiscovering;
import com.yoursway.utils.InterpreterRunnerUtil;

public class RubyInstallWrapper {
    
    public static final String AMBIG_NAME_NUMBER_DELIMITER = " #";
    
    private final IInterpreterInstall interpreterInstall;
    
    public RubyInstallWrapper(IInterpreterInstall interpreterInstall) {
        this.interpreterInstall = interpreterInstall;
    }
    
    public String getId() {
        return interpreterInstall.getId();
    }
    
    public static Collection<? extends RubyInstallWrapper> getAll() {
        IInterpreterInstallType rubyInstallType = RubyAndRailsDiscovering.getRubyInstallType();
        
        Collection<RubyInstallWrapper> result = new ArrayList<RubyInstallWrapper>();
        for (IInterpreterInstall rubyInterpreter : rubyInstallType.getInterpreterInstalls()) {
            result.add(new RubyInstallWrapper(rubyInterpreter));
        }
        return result;
    }
    
    public static RubyInstallWrapper create(File location, String name) {
        IInterpreterInstallType rubyInstallType = RubyAndRailsDiscovering.getRubyInstallType();
        Set<String> usedIds = collectAllUsedIds(rubyInstallType);
        String uniqueId = StringUtils.chooseUniqueString(name, "", AMBIG_NAME_NUMBER_DELIMITER, usedIds);
        IInterpreterInstall install = rubyInstallType.createInterpreterInstall(uniqueId);
        install.setName(name);
        install.setInstallLocation(location);
        return new RubyInstallWrapper(install);
    }
    
    private static Set<String> collectAllUsedIds(IInterpreterInstallType rubyInstallType) {
        IInterpreterInstall[] installs = rubyInstallType.getInterpreterInstalls();
        Set<String> usedIds = new HashSet<String>();
        for (IInterpreterInstall install : installs)
            usedIds.add(install.getId());
        return usedIds;
    }
    
    public void destroy() {
        IInterpreterInstallType rubyInstallType = RubyAndRailsDiscovering.getRubyInstallType();
        rubyInstallType.disposeInterpreterInstall(interpreterInstall.getId());
    }
    
    public File getLocation() {
        return interpreterInstall.getInstallLocation();
    }
    
    /**
     * @param fileName
     * @param arguments
     * @param monitor
     *            the progress monitor to use for reporting progress to the
     *            user. It is the caller's responsibility to call done() on the
     *            given monitor. Accepts <code>null</code>, indicating that
     *            no progress should be reported and that the operation cannot
     *            be cancelled.
     * @return
     * @throws RubyScriptInvokationError
     */
    public ToolExecutionResult runRubyScript(String fileName, List<String> arguments, IProgressMonitor monitor)
            throws RubyScriptInvokationError {
        SubMonitor progress = SubMonitor.convert(monitor);
        InterpreterConfig config = createInterpreterConfig(fileName, arguments);
        ILaunch launch = null;
        try {
            launch = InterpreterRunnerUtil.run(interpreterInstall, config, progress
                    .newChild(IProgressMonitor.UNKNOWN));
        } catch (CoreException e) {
            Activator.reportException(e, "Running a tool");
            throw new RubyScriptInvokationError(e);
        }
        
        return getLaunchResult(launch);
    }
    
    /**
     * Starts given Ruby script and returns launch immediately.
     * 
     * @param fileName
     *            Ruby script to start
     * @param arguments
     *            arguments for Ruby script
     * @return running launch
     * @throws RubyScriptInvokationError
     */
    public ILaunch startRubyScript(String fileName, List<String> arguments) throws RubyScriptInvokationError {
        InterpreterConfig config = createInterpreterConfig(fileName, arguments);
        try {
            return InterpreterRunnerUtil.start(interpreterInstall, config);
        } catch (CoreException e) {
            Activator.reportException(e, "Starting a tool");
            throw new RubyScriptInvokationError(e);
        }
    }
    
    /**
     * Extracts launch result from the finished ILaunch.
     * 
     * @param launch
     *            finished ILaunch
     * @return launch result
     */
    private ToolExecutionResult getLaunchResult(ILaunch launch) {
        IProcess[] launchProcesses = launch.getProcesses();
        assert launchProcesses.length == 1 : "Ruby script launch is expected to have single process";
        
        IProcess launchProcess = launchProcesses[0];
        
        final int exitCode = InterpreterRunnerUtil.getFinishedProcessExitValue(launchProcess);
        String output = launchProcess.getStreamsProxy().getOutputStreamMonitor().getContents();
        String error = launchProcess.getStreamsProxy().getErrorStreamMonitor().getContents();
        return new ToolExecutionResult(exitCode, output, error);
    }
    
    /**
     * Generates InterpreterConfig for the DebugPlugin from the given Ruby
     * script filename and command arguments.
     * 
     * @param fileName
     *            script filename
     * @param arguments
     *            command arguments
     * @return created InterpreterConfig
     */
    private InterpreterConfig createInterpreterConfig(String fileName, List<String> arguments) {
        InterpreterConfig config = new InterpreterConfig(new File(fileName));
        config.addScriptArgs(arguments.toArray(new String[arguments.size()]));
        config.addEnvVars(System.getenv());
        return config;
    }
    
    public IInterpreterInstall getRawDLTKInterpreterInstall() {
        return interpreterInstall;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final RubyInstallWrapper other = (RubyInstallWrapper) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }
    
}
