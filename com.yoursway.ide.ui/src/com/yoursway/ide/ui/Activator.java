package com.yoursway.ide.ui;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.yoursway.ide.analysis.sample.ControllerNavigationProviderFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
    
    public static final String PLUGIN_ID = "com.yoursway.ide.ui";
    
    public static boolean GENERAL_DEBUG = Boolean.parseBoolean(Platform
            .getDebugOption("com.yoursway.ide.ui/generalDebug"));
    
    private static boolean LOG_EXCEPTIONS_TO_CONSOLE = Boolean.parseBoolean(Platform
            .getDebugOption("com.yoursway.ide.ui/logExceptionsToConsole"));
    
    private static Activator plugin;
    
    public Activator() {
    }
    
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        ControllerNavigationProviderFactory.initialize();
    }
    
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }
    
    public static Activator getDefault() {
        return plugin;
    }
    
    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     * 
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
    
    public static void log(Throwable e) {
        if (LOG_EXCEPTIONS_TO_CONSOLE)
            e.printStackTrace(System.err);
        getDefault().getLog().log(new Status(Status.ERROR, PLUGIN_ID, e.getMessage(), e));
    }
}
