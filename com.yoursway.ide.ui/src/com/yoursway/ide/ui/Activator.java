package com.yoursway.ide.ui;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.yoursway.ide.analysis.sample.ControllerNavigationProviderFactory;
import com.yoursway.rails.RailsInfoRefreshRunner;

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
        
        RailsInfoRefreshRunner.startListening();
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
        log(e, null);
    }
    
    public static void log(Throwable e, String additionalMessage) {
        if (LOG_EXCEPTIONS_TO_CONSOLE)
            e.printStackTrace(System.err);
        String message = e.getMessage();
        if (additionalMessage != null)
            message = additionalMessage + ": " + message;
        getDefault().getLog().log(new Status(Status.ERROR, PLUGIN_ID, message, e));
    }
    
    public static void log(String string) {
        if (LOG_EXCEPTIONS_TO_CONSOLE)
            System.err.println(string);
        getDefault().getLog().log(new Status(Status.ERROR, PLUGIN_ID, string, null));
    }
    
    /**
     * Returns a section in the plugin's dialog settings. If the section doesn't
     * exist yet, it is created.
     * 
     * @param name
     *            the name of the section
     * @return the section of the given name
     */
    public static IDialogSettings getDialogSettingsSection(String name) {
        IDialogSettings dialogSettings = getDefault().getDialogSettings();
        IDialogSettings section = dialogSettings.getSection(name);
        if (section == null) {
            section = dialogSettings.addNewSection(name);
        }
        return section;
    }
    
    /**
     * Use this method instead of real assertions to code defensively. Warning:
     * only use when you are sure that you can recover completely.
     * 
     * @param condition
     *            the result of a check that should always be true
     * @return the boolean passed in (to allow caller to conditionally invoke
     *         the recovering code)
     */
    public static boolean softAssert(boolean condition) {
        if (!condition) {
            try {
                throw new Exception("Soft Assertion Failure");
            } catch (Exception e) {
                log(e);
            }
        }
        return condition;
    }
    
    public static void reportException(Throwable e, String failedUserActionMessage) {
        log(e, failedUserActionMessage);
        MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                failedUserActionMessage, "The following error occured:\n\n" + e.getMessage() + "\n\n"
                        + "See error log for details.");
    }
}
