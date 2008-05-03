package com.yoursway.ide.rcp.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.yoursway.ide.rcp";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	public static void log(String message) {
//	    getDefault().getLog().log(new Status(Status.ERROR, PLUGIN_ID, message));
	    if (Platform.inDebugMode()) {
	        System.err.println(message);
	    }
	}
	public static void log(String message, Throwable throwable) {
//        getDefault().getLog().log(new Status(Status.ERROR, PLUGIN_ID, message, throwable));
        if (Platform.inDebugMode()) {
            System.err.println(message);
            throwable.printStackTrace(System.err);
        }
    }
	
	public static void log(Throwable throwable) {
//	    getDefault().getLog().log(new Status(Status.ERROR, PLUGIN_ID, throwable.getMessage(), throwable));
	    if (Platform.inDebugMode()) {
	        throwable.printStackTrace(System.err);
	    }
	}

    public static void log(String message, IStatus status) {
        // this code is crazy, but copied from the Platform
        if (message != null)
            log(message);
//        getDefault().getLog().log(status);
    }

}
