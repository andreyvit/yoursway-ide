package com.yoursway.introspection.internal.launches;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener2;

public class LaunchesObserver implements ILaunchesListener2 {
    
    private List<String> launchesInfo = new ArrayList<String>();
    
    public List<String> getLaunchesInfo() {
        return launchesInfo;
    }
    
    public void launchesTerminated(ILaunch[] launches) {
        for (ILaunch l : launches)
            launchesInfo.add("Terminated: " + l.toString());
    }
    
    public void launchesAdded(ILaunch[] launches) {
        for (ILaunch l : launches)
            launchesInfo.add("Added: " + l.toString());
    }
    
    public void launchesChanged(ILaunch[] launches) {
    }
    
    public void launchesRemoved(ILaunch[] launches) {
    }
}
