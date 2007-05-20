package com.yoursway.ruby;

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
import org.eclipse.dltk.launching.InterpreterRunnerConfiguration;
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
     */
    public ToolExecutionResult runRubyScript(String fileName, List<String> arguments, IProgressMonitor monitor) {
        SubMonitor progress = SubMonitor.convert(monitor);
        InterpreterRunnerConfiguration config = new InterpreterRunnerConfiguration(fileName);
        config.setProgramArguments(arguments.toArray(new String[arguments.size()]));
        
        ILaunch launch = null;
        try {
            launch = InterpreterRunnerUtil.run(interpreterInstall, config, progress
                    .newChild(IProgressMonitor.UNKNOWN));
        } catch (CoreException e) {
            Activator.reportException(e, "Running a tool");
        }
        
        IProcess[] launchProcesses = launch.getProcesses();
        assert launchProcesses.length == 1 : "Ruby script launch is expected to have single process";
        
        IProcess launchProcess = launchProcesses[0];
        
        final int exitCode = InterpreterRunnerUtil.getFinishedProcessExitValue(launchProcess);
        String output = launchProcess.getStreamsProxy().getOutputStreamMonitor().getContents();
        return new ToolExecutionResult(exitCode, output);
    }
    
}
