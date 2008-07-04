package com.yoursway.ide.undo;

import java.io.File;
import java.io.FileReader;

import org.eclipse.core.resources.ISavedState;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.yoursway.ide.undo";

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
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
        ISavedState lastState = workspace.addSaveParticipant(plugin, new MyWorkspaceSaveParticipant());
        if (lastState == null)
            return;
        
        IPath location = lastState.lookup(new Path("operationHistory"));
        if (location == null)
            return;
        
        File f = getStateLocation().append(location).toFile();
        
        IMemento memento = XMLMemento.createReadRoot(new FileReader(f));
        OperationHistory.init(memento);
        
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

}
