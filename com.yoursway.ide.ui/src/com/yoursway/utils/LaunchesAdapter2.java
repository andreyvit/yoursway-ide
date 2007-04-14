package com.yoursway.utils;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener2;

/**
 * Abstract adapter class for receiving launches state change events. The
 * methods in this class are empty. This class exists as convenience for
 * creating listener objects.
 * 
 * @see ILaunchesListener2
 */
public abstract class LaunchesAdapter2 implements ILaunchesListener2 {
    
    public void launchesTerminated(ILaunch[] launches) {
    }
    
    public void launchesAdded(ILaunch[] launches) {
    }
    
    public void launchesChanged(ILaunch[] launches) {
    }
    
    public void launchesRemoved(ILaunch[] launches) {
    }
}
