package com.yoursway.utils;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.dltk.launching.AbstractInterpreterRunner;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterRunner;
import org.eclipse.dltk.launching.InterpreterConfig;

import com.yoursway.common.StringListBuilder;

/**
 * Utility class wrapping {@link AbstractInterpreterRunner} with convenience
 * functions.
 */
public final class InterpreterRunnerUtil {
    
    /**
     * Runs the specified interpreter in the specified configuration, awaiting
     * the finish and returning the finished launch. Returns null if launching
     * was terminated.
     * 
     * @param install
     *            interpreter to run
     * @param config
     *            program configuration to run
     * @param monitor
     *            monitor to display information progress. it is responsibility
     *            of caller to call done() on monitor. null may be passed if no
     *            progress reporting/cancelation is required.
     * 
     * @return finished launch with information about launch
     * @throws CoreException
     */
    public static ILaunch run(IInterpreterInstall install, InterpreterConfig config, IProgressMonitor monitor)
            throws CoreException {
        
        SubMonitor progress = SubMonitor.convert(monitor);
        
        final ILaunch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
        
        IInterpreterRunner runner = install.getInterpreterRunner(ILaunchManager.RUN_MODE);
        runner.run(config, launch, null);
        
        /*
         * This loop is the workaround. ILaunch does not provide event
         * listeners, so all launch listeners need to be registered in
         * ILaunchManager. Adding launch to the ILaunchManager automatically
         * associates it with the console, which sucks away the stdout/stderr of
         * launch preventing it from the processing.
         * 
         * It is impossible to create our own implementation of ILaunchManager
         * and register ILaunch in this implementation, because Launch (the
         * default implementation of ILaunch) directly refers LaunchManager (the
         * default implementation of ILaunchManager).
         */
        while (!launch.isTerminated())
            try {
                if (progress.isCanceled()) {
                    launch.terminate();
                    return null;
                }
                progress.worked(1);
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
        
        return launch;
    }
    
    //FIXME: move the functions below to the utility class.
    
    /**
     * This function converts the set of arguments to string representation of
     * command line. The output is directly copy-pasteable to the shell.
     */
    public static String convertCommandLineToString(String[] args) {
        return convertCommandLineToString(Arrays.asList(args));
    }
    
    /**
     * This function converts the set of arguments to string representation of
     * command line. The output is directly copy-pasteable to the shell.
     */
    public static String convertCommandLineToString(Collection<String> args) {
        StringListBuilder result = new StringListBuilder(" ");
        for (String arg : args)
            result.append(wrapArgument(arg));
        return result.toString();
    }
    
    /**
     * This function wraps the passed argument in the OS-DEPENDENT manner,
     * emitting the argument ready to be copy-pasted to the native shell.
     * 
     * @param arg
     *            command-line argument to be wrapped
     * @return wrapped command-line argument
     */
    private static String wrapArgument(String arg) {
        if (Platform.getOS().equals(Platform.OS_WIN32))
            return wrapWindowsArgument(arg);
        else
            return wrapUnixArgument(arg);
    }
    
    /**
     * This function wraps the argument escaping all the special for the Unix
     * shell characters to make supplied argument directly pasteable to the Unix
     * shell.
     * 
     * @param arg
     *            command-line argument to be wrapped.
     * @return wrapped command-line argument
     */
    private static String wrapUnixArgument(String arg) {
        /*
         * The following characters need to be quoted: |&;<>()$`\"' \n\r\t
         * 
         * Arguments with special characters, but without ' are quoted in '.
         * Arguments with special characters and ' are wrapped in ", escaping
         * the $\`"
         */

        if (arg.contains("|&;<>()$`\\\"' \r\n\t")) {
            if (!arg.contains("'"))
                return "'" + arg + "'";
            
            return "\"" + escapeDoubleQuoteMetacharacters(arg.toCharArray()) + "\"";
        }
        return arg;
    }
    
    /**
     * This function escapes all characters which are special in double quotes
     * in POSIX shell.
     */
    private static String escapeDoubleQuoteMetacharacters(char[] arg) {
        StringBuffer res = new StringBuffer();
        for (char c : arg) {
            if (c == '$' || c == '\\' || c == '`' || c == '"')
                res.append('\\');
            res.append(c);
        }
        return "\"" + res.toString() + "\"";
    }
    
    /**
     * This function wraps the argument escaping all the special for the Windows
     * CMD shell characters to make supplied argument directly pasteable to the
     * Windows console.
     * 
     * FIXME: This function does not quote double quotes properly.
     * 
     * @param arg
     *            command-line argument to be wrapped
     * @return wrapped command-line argument
     */
    private static String wrapWindowsArgument(String arg) {
        if (arg.contains(" \t\r\n"))
            return "\"" + arg + "\"";
        return arg;
    }
    
    /**
     * Returns the exit value from the finished process.
     * 
     * @see IProcess#getExitValue()
     * @param launchProcess
     *            process to get exit value
     * @return process exit value
     */
    public static int getFinishedProcessExitValue(IProcess launchProcess) {
        try {
            return launchProcess.getExitValue();
        } catch (DebugException e) {
            throw new AssertionError("DebugError won't be thrown on finished process");
        }
    }
}
