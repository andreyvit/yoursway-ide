package com.yoursway.ide.ui;

import org.eclipse.ui.WorkbenchException;

import com.yoursway.ide.projects.YourSwayProjects;
import com.yoursway.rails.RailsInfoRefreshRunner;
import com.yoursway.rails.discovering.RubyAndRailsDiscovering;

public class YourSwayIDE {
    
    public static void finishLoading() {
        try {
            YourSwayProjects.makeSureAllWindowsExist();
        } catch (WorkbenchException e) {
            Activator.unexpectedError(e);
        }
        
        RubyAndRailsDiscovering.initialize();
        RailsInfoRefreshRunner.startListening();
    }
    
}
