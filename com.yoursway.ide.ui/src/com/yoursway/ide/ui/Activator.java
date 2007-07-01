package com.yoursway.ide.ui;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.yoursway.ide.analysis.sample.ControllerNavigationProviderFactory;
import com.yoursway.rails.RailsInfoRefreshRunner;
import com.yoursway.rails.discovering.RubyAndRailsDiscovering;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
    
    /**
     * @see http://java.sun.com/j2se/1.4.2/docs/guide/lang/assert.html,
     *      "Requiring that Assertions are Enabled"
     */
    static {
        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (!assertsEnabled) {
            throw new RuntimeException("Asserts must be enabled!!!");
        }
    }
    
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
        
        RubyAndRailsDiscovering.initialize();
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
    
    /**
     * @deprecated use <code>unexpectedError</code> instead
     */
    @Deprecated
    public static void log(Throwable e) {
        unexpectedError(e);
    }
    
    /**
     * @deprecated use <code>unexpectedError</code> instead
     */
    @Deprecated
    public static void log(Throwable e, String additionalMessage) {
        unexpectedError(e, additionalMessage);
    }
    
    /**
     * @deprecated use <code>unexpectedError</code> instead
     */
    @Deprecated
    public static void log(String string) {
        unexpectedError(string);
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
    
    public static void unexpectedError(Throwable e) {
        unexpectedError(e, null);
    }
    
    public static void unexpectedError(Throwable e, String additionalMessage) {
        if (LOG_EXCEPTIONS_TO_CONSOLE)
            e.printStackTrace(System.err);
        String message = e.getMessage();
        if (additionalMessage != null)
            message = additionalMessage + ": " + message;
        getDefault().getLog().log(new Status(Status.ERROR, PLUGIN_ID, message, e));
    }
    
    public static void unexpectedError(String message) {
        if (LOG_EXCEPTIONS_TO_CONSOLE)
            System.err.println(message);
        getDefault().getLog().log(new Status(Status.ERROR, PLUGIN_ID, message, null));
    }
    
    public static void reportException(final Throwable e, final String failedUserActionMessage) {
        log(e, failedUserActionMessage);
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        failedUserActionMessage, "The following error occured:\n\n" + e.getMessage() + "\n\n"
                                + "See error log for details.");
            }
        });
    }
    
    /**
     * Temporary, for copying JSP editor.
     */
    public static void unexpectedError(String string, Throwable e) {
        unexpectedError(e, string);
    }
}
