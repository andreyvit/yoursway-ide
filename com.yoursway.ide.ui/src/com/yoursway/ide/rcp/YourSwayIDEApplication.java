package com.yoursway.ide.rcp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.yoursway.ide.ui.Activator;
import com.yoursway.utils.SystemUtilities;

public class YourSwayIDEApplication implements IApplication, IExecutableExtension {
    
    private static final String WORKSPACE_CANNOT_BE_CREATED = "Workspace Cannot Be Created";
    
    /**
     * The name of the folder containing metadata information for the workspace.
     */
    public static final String METADATA_FOLDER = ".metadata"; //$NON-NLS-1$
    
    private static final String VERSION_FILENAME = "version.ini"; //$NON-NLS-1$
    
    private static final String WORKSPACE_VERSION_KEY = "org.eclipse.core.runtime"; //$NON-NLS-1$
    
    private static final String WORKSPACE_VERSION_VALUE = "1"; //$NON-NLS-1$
    
    private static final String PROP_EXIT_CODE = "eclipse.exitcode"; //$NON-NLS-1$
    
    /**
     * A special return code that will be recognized by the launcher and used to
     * restart the workbench.
     */
    private static final Integer EXIT_RELAUNCH = new Integer(24);
    
    /**
     * Creates a new IDE application.
     */
    public YourSwayIDEApplication() {
        // There is nothing to do for YourSwayIDEApplication
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext
     *      context)
     */
    public Object start(IApplicationContext appContext) throws Exception {
        Display display = createDisplay();
        
        try {
            Shell shell = new Shell(display, SWT.ON_TOP);
            
            try {
                if (!checkInstanceLocation(shell)) {
                    Platform.endSplash();
                    return EXIT_OK;
                }
            } finally {
                if (shell != null) {
                    shell.dispose();
                }
            }
            
            // create the workbench with this advisor and run it until it exits
            // N.B. createWorkbench remembers the advisor, and also registers
            // the workbench globally so that all UI plug-ins can find it using
            // PlatformUI.getWorkbench() or AbstractUIPlugin.getWorkbench()
            int returnCode = PlatformUI.createAndRunWorkbench(display, new IDEWorkbenchAdvisor());
            
            // the workbench doesn't support relaunch yet (bug 61809) so
            // for now restart is used, and exit data properties are checked
            // here to substitute in the relaunch return code if needed
            if (returnCode != PlatformUI.RETURN_RESTART) {
                return EXIT_OK;
            }
            
            // if the exit code property has been set to the relaunch code, then
            // return that code now, otherwise this is a normal restart
            return EXIT_RELAUNCH.equals(Integer.getInteger(PROP_EXIT_CODE)) ? EXIT_RELAUNCH : EXIT_RESTART;
        } finally {
            if (display != null) {
                display.dispose();
            }
        }
    }
    
    /**
     * Creates the display used by the application.
     * 
     * @return the display used by the application
     */
    protected Display createDisplay() {
        return PlatformUI.createDisplay();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement,
     *      java.lang.String, java.lang.Object)
     */
    public void setInitializationData(IConfigurationElement config, String propertyName, Object data) {
        // There is nothing to do for IDEApplication
    }
    
    /**
     * Return true if a valid workspace path has been set and false otherwise.
     * Prompt for and set the path if possible and required.
     * 
     * @return true if a valid instance location has been set and false
     *         otherwise
     */
    private boolean checkInstanceLocation(Shell shell) {
        Location instanceLoc = Platform.getInstanceLocation();
        if (instanceLoc == null) {
            // "-data @none" was specified
            MessageDialog
                    .openError(shell, "Workspace is Mandatory",
                            "IDE really needs a valid workspace. Restart without the @none option. Thanks in advance!");
            return false;
        }
        
        // -data "/valid/path" was specified
        if (instanceLoc.isSet()) {
            // make sure the meta data version is compatible (or the user has
            // chosen to overwrite it).
            if (!checkValidWorkspace(shell, instanceLoc.getURL())) {
                return false;
            }
            
            // at this point its valid, so try to lock it and update the
            // metadata version information if successful
            try {
                if (instanceLoc.lock()) {
                    writeWorkspaceVersion();
                    return true;
                }
                
                // we failed to create the directory.  
                // Two possibilities:
                // 1. directory is already in use
                // 2. directory could not be created
                File workspaceDirectory = new File(instanceLoc.getURL().getFile());
                if (workspaceDirectory.exists()) {
                    MessageDialog
                            .openError(
                                    shell,
                                    "Workspace Cannot Be Locked",
                                    "Could not launch the product because the associated workspace is currently in use by another Eclipse application.");
                } else {
                    MessageDialog
                            .openError(
                                    shell,
                                    WORKSPACE_CANNOT_BE_CREATED,
                                    "Could not launch the product because the specified workspace cannot be created.  The specified workspace directory is either invalid or read-only.");
                }
            } catch (IOException e) {
                Activator.reportException(e, "Could not obtain lock for workspace location");
            }
            return false;
        }
        
        URL workspaceUrl;
        final File workspaceFolder;
        try {
            workspaceFolder = SystemUtilities.getInstance().getRCPWorkspaceStorageLocation(YourSwayRCP.NAME);
            if (!workspaceFolder.exists())
                workspaceFolder.mkdir();
            
            // Don't use File.toURL() since it adds a leading slash that Platform does not
            // handle properly.  See bug 54081 for more details.  
            String path = workspaceFolder.getAbsolutePath().replace(File.separatorChar, '/');
            workspaceUrl = new URL("file", null, path); //$NON-NLS-1$
        } catch (MalformedURLException e) {
            Activator.unexpectedError(e);
            return false;
        }
        
        try {
            // the operation will fail if the url is not a valid
            // instance data area, so other checking is unneeded
            if (instanceLoc.setURL(workspaceUrl, true)) {
                writeWorkspaceVersion();
                return true;
            }
        } catch (IllegalStateException e) {
            MessageDialog.openError(shell, WORKSPACE_CANNOT_BE_CREATED, NLS.bind(
                    "Cannot launch YourSway IDE because directory {0} cannot be created.", workspaceFolder));
            return false;
        }
        
        MessageDialog.openError(shell, "Workspace Unavailable", "Workspace in use or cannot be created.");
        return false;
    }
    
    /**
     * Return true if the argument directory is ok to use as a workspace and
     * false otherwise. A version check will be performed, and a confirmation
     * box may be displayed on the argument shell if an older version is
     * detected.
     * 
     * @return true if the argument URL is ok to use as a workspace and false
     *         otherwise.
     */
    private boolean checkValidWorkspace(Shell shell, URL url) {
        // a null url is not a valid workspace
        if (url == null) {
            return false;
        }
        
        String version = readWorkspaceVersion(url);
        
        // if the version could not be read, then there is not any existing
        // workspace data to trample, e.g., perhaps its a new directory that
        // is just starting to be used as a workspace
        if (version == null) {
            return true;
        }
        
        final int ide_version = Integer.parseInt(WORKSPACE_VERSION_VALUE);
        int workspace_version = Integer.parseInt(version);
        
        // equality test is required since any version difference (newer
        // or older) may result in data being trampled
        if (workspace_version == ide_version) {
            return true;
        }
        
        // At this point workspace has been detected to be from a version
        // other than the current ide version -- find out if the user wants
        // to use it anyhow.
        String title = "Different Workspace Version";
        String message = NLS
                .bind(
                        "This workspace was written with a different version of the product and needs to be updated.\n\n{0}\n\nUpdating the workspace may make it incompatible with other versions of the product.\nPress OK to update the workspace and open it.  Press Cancel to select a different workspace.",
                        url.getFile());
        
        MessageBox mbox = new MessageBox(shell, SWT.OK | SWT.CANCEL | SWT.ICON_WARNING
                | SWT.APPLICATION_MODAL);
        mbox.setText(title);
        mbox.setMessage(message);
        return mbox.open() == SWT.OK;
    }
    
    /**
     * Look at the argument URL for the workspace's version information. Return
     * that version if found and null otherwise.
     */
    public static String readWorkspaceVersion(URL workspace) {
        File versionFile = getVersionFile(workspace, false);
        if (versionFile == null || !versionFile.exists()) {
            return null;
        }
        
        try {
            // Although the version file is not spec'ed to be a Java properties
            // file, it happens to follow the same format currently, so using
            // Properties to read it is convenient.
            Properties props = new Properties();
            FileInputStream is = new FileInputStream(versionFile);
            try {
                props.load(is);
            } finally {
                is.close();
            }
            
            return props.getProperty(WORKSPACE_VERSION_KEY);
        } catch (IOException e) {
            Activator.reportException(e, NLS.bind("Could not read version file {0}", versionFile));
            return null;
        }
    }
    
    /**
     * Write the version of the metadata into a known file overwriting any
     * existing file contents. Writing the version file isn't really crucial, so
     * the function is silent about failure
     */
    private static void writeWorkspaceVersion() {
        Location instanceLoc = Platform.getInstanceLocation();
        if (instanceLoc == null || instanceLoc.isReadOnly()) {
            return;
        }
        
        File versionFile = getVersionFile(instanceLoc.getURL(), true);
        if (versionFile == null) {
            return;
        }
        
        OutputStream output = null;
        try {
            String versionLine = WORKSPACE_VERSION_KEY + '=' + WORKSPACE_VERSION_VALUE;
            
            output = new FileOutputStream(versionFile);
            output.write(versionLine.getBytes("UTF-8")); //$NON-NLS-1$
        } catch (IOException e) {
            Activator.reportException(e, NLS.bind("Could not write version file {0}", versionFile));
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                // do nothing
            }
        }
    }
    
    /**
     * The version file is stored in the metadata area of the workspace. This
     * method returns an URL to the file or null if the directory or file does
     * not exist (and the create parameter is false).
     * 
     * @param create
     *            If the directory and file does not exist this parameter
     *            controls whether it will be created.
     * @return An url to the file or null if the version file does not exist or
     *         could not be created.
     */
    private static File getVersionFile(URL workspaceUrl, boolean create) {
        if (workspaceUrl == null) {
            return null;
        }
        
        try {
            // make sure the directory exists
            File metaDir = new File(workspaceUrl.getPath(), METADATA_FOLDER);
            if (!metaDir.exists() && (!create || !metaDir.mkdir())) {
                return null;
            }
            
            // make sure the file exists
            File versionFile = new File(metaDir, VERSION_FILENAME);
            if (!versionFile.exists() && (!create || !versionFile.createNewFile())) {
                return null;
            }
            
            return versionFile;
        } catch (IOException e) {
            // cannot log because instance area has not been set
            return null;
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.equinox.app.IApplication#stop()
     */
    public void stop() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench == null)
            return;
        final Display display = workbench.getDisplay();
        display.syncExec(new Runnable() {
            public void run() {
                if (!display.isDisposed())
                    workbench.close();
            }
        });
    }
}
