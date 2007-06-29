package com.yoursway.ruby;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.ruby.core.RubyNature;

import com.yoursway.ide.ui.Activator;
import com.yoursway.utils.InterpreterRunnerUtil;

public class RubyInstallation {
    
    private final IInterpreterInstall interpreterInstall;
    
    private RubyInstallation(IInterpreterInstall interpreterInstall) {
        this.interpreterInstall = interpreterInstall;
    }
    
    public static Collection<? extends RubyInstallation> getRubyInstallations() {
        IInterpreterInstallType[] rubyInterpreters = ScriptRuntime
                .getInterpreterInstallTypes(RubyNature.NATURE_ID);
        assert rubyInterpreters.length == 1 : "Only one IInterpreterInstallType is expected for Ruby nature";
        
        Collection<RubyInstallation> result = new ArrayList<RubyInstallation>();
        for (IInterpreterInstall rubyInterpreter : rubyInterpreters[0].getInterpreterInstalls()) {
            result.add(adapt(rubyInterpreter));
        }
        return result;
    }
    
    public static RubyInstallation adapt(IInterpreterInstall interpreterInstall) {
        return new RubyInstallation(interpreterInstall);
    }
    
    public static class RubyScriptInvokationError extends Exception {
        
        private static final long serialVersionUID = 1L;
        
        public RubyScriptInvokationError() {
            super();
        }
        
        public RubyScriptInvokationError(String message, Throwable cause) {
            super(message, cause);
        }
        
        public RubyScriptInvokationError(String message) {
            super(message);
        }
        
        public RubyScriptInvokationError(Throwable cause) {
            super(cause);
        }
        
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
        InterpreterConfig config = new InterpreterConfig(new File(fileName));
        config.addScriptArgs(arguments.toArray(new String[arguments.size()]));
        config.addEnvVars(System.getenv());
        ILaunch launch = null;
        try {
            launch = InterpreterRunnerUtil.run(interpreterInstall, config, progress
                    .newChild(IProgressMonitor.UNKNOWN));
        } catch (CoreException e) {
            Activator.reportException(e, "Running a tool");
            throw new RubyScriptInvokationError(e);
        }
        
        IProcess[] launchProcesses = launch.getProcesses();
        assert launchProcesses.length == 1 : "Ruby script launch is expected to have single process";
        
        IProcess launchProcess = launchProcesses[0];
        
        final int exitCode = InterpreterRunnerUtil.getFinishedProcessExitValue(launchProcess);
        String output = launchProcess.getStreamsProxy().getOutputStreamMonitor().getContents();
        String error = launchProcess.getStreamsProxy().getErrorStreamMonitor().getContents();
        return new ToolExecutionResult(exitCode, output, error);
    }
}
