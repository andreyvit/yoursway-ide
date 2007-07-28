package com.yoursway.ruby;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

import org.eclipse.core.runtime.IProgressMonitor;

import com.yoursway.ide.ui.Activator;

public class RubyToolUtils {
    
    /**
     * Sends SIGINT to the specified process, asking it to shut down gracefully.
     */
    public static void sendInterruptSignal(RubyInstance ruby, int pid, IProgressMonitor monitor) {
        sendSignal(ruby, "INT", pid, monitor);
    }
    
    /**
     * Sends SIGKILL to the specified process, effectively terminating it.
     */
    public static void sendKillSignal(RubyInstance ruby, int pid, IProgressMonitor monitor) {
        sendSignal(ruby, "KILL", pid, monitor);
    }
    
    /**
     * Sends a specified signal to the specified process using a Ruby script.
     * 
     * @param ruby
     *            the Ruby installation to use for invoking the killing script.
     * @param signalName
     *            a POSIX signal name (or number) one could pass to kill(1);
     *            e.g. "INT", "KILL", "9".
     * @param pid
     *            the process identifier of the process to kill.
     * @param monitor
     *            the progress monitor to use for reporting progress to the
     *            user. It is the caller's responsibility to call done() on the
     *            given monitor. Accepts <code>null</code>, indicating that
     *            no progress should be reported and that the operation cannot
     *            be cancelled.
     */
    public static void sendSignal(RubyInstance ruby, String signalName, int pid, IProgressMonitor monitor) {
        String scriptPath = getScriptCopySuitableForRunning("kill.rb");
        String[] args = new String[] { "INT", "" + pid };
        try {
            ruby.runRubyScript(scriptPath, Arrays.asList(args), monitor);
        } catch (RubyScriptInvokationError e) {
            Activator.reportException(e, "Stopping a process");
        }
    }
    
    /**
     * Writes a script from plugin jar to plugin metadata folder within the
     * workspace.
     * 
     * @param rubyFile -
     *            The file to place on the filesystem
     * @return Absolute path to specified script file
     */
    public static String getScriptCopySuitableForRunning(String rubyFile) {
        String directoryFile = Activator.getDefault().getStateLocation().toOSString() + File.separator
                + rubyFile;
        File pluginDirFile = new File(directoryFile);
        
        // TODO: caching disabled for debugging
        if (true || !pluginDirFile.exists()) {
            try {
                pluginDirFile.createNewFile();
                URL u = Activator.getDefault().getBundle().getEntry("/scripts/" + rubyFile);
                BufferedReader input = new BufferedReader(new InputStreamReader(u.openStream()));
                FileWriter output = new FileWriter(pluginDirFile);
                String line;
                while ((line = input.readLine()) != null) {
                    output.write(line);
                    output.write('\n');
                }
                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                Activator.unexpectedError(e, "Error copying script file from jar to metadata");
            }
        }
        
        String path = "";
        try {
            path = pluginDirFile.getCanonicalPath();
        } catch (IOException e) {
            Activator.unexpectedError(e, "Error getting script file path");
        }
        return path;
    }
}
