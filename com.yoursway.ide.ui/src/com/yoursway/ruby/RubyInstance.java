package com.yoursway.ruby;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;

import com.yoursway.rails.Version;

public interface RubyInstance {
    
    Version getVersion();
    
    File getLocation();
    
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
    ProcessResult runRubyScript(String fileName, List<String> arguments, IProgressMonitor monitor)
            throws RubyScriptInvokationError;
    
    /**
     * Starts Ruby script, returning the ILaunch.
     * 
     * @param fileName
     * @param arguments
     * @return
     * @throws RubyScriptInvokationError
     */
    ILaunch startRubyScript(String fileName, List<String> arguments) throws RubyScriptInvokationError;
    
    //String getId();
}
