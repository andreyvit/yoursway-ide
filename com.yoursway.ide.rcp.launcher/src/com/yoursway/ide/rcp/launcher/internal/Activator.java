package com.yoursway.ide.rcp.launcher.internal;

import java.io.IOException;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.yoursway.ide.rcp.launcher";

    public static void logWhenNoWorkspace(String string, IOException e) {
        System.err.println(string);
        e.printStackTrace(System.err);
    }

    public static void log(String string, IOException e) {
        com.yoursway.ide.rcp.internal.Activator.log(string, e);
    }

}
