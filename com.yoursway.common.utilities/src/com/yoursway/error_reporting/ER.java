package com.yoursway.error_reporting;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import com.yoursway.common.utilities.Activator;

public class ER {
    
    private static final String PLUGIN_ID = "com.yoursway.common.utilities";
    
    private static boolean LOG_EXCEPTIONS_TO_CONSOLE = Boolean.parseBoolean(Platform
            .getDebugOption("com.yoursway.common.utilities/logExceptionsToConsole"));
    
    private void log(Throwable e, String additionalMessage) {
        if (LOG_EXCEPTIONS_TO_CONSOLE)
            e.printStackTrace(System.err);
        String message = e.getMessage();
        if (additionalMessage != null)
            message = additionalMessage + ": " + message;
        Activator.getDefault().getLog().log(new Status(Status.ERROR, PLUGIN_ID, message, e));
    }
    
    public void unexpected(Throwable e) {
        unexpected(e, null);
    }
    
    public void unexpected(Throwable e, String additionalMessage) {
        log(e, additionalMessage);
    }
    
    public void expected(Throwable e) {
        expected(e, null);
    }
    
    public void expected(Throwable e, String additionalMessage) {
        log(e, additionalMessage);
    }
    
}
