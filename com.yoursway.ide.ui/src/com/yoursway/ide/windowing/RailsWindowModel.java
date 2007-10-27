package com.yoursway.ide.windowing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.yoursway.common.TypedListenerList;
import com.yoursway.ide.projects.YourSwayProjects;
import com.yoursway.rails.core.projects.RailsProject;

public class RailsWindowModel {
    
    private static final RailsWindowModel INSTANCE = new RailsWindowModel();
    
    private final Map<IWorkbenchWindow, RailsWindow> mapping = new HashMap<IWorkbenchWindow, RailsWindow>();
    
    private final TypedListenerList<IRailsWindowModelListener> listeners = new TypedListenerList<IRailsWindowModelListener>();
    
    public static RailsWindowModel instance() {
        return INSTANCE;
    }
    
    private RailsWindowModel() {
        PlatformUI.getWorkbench().addWindowListener(new IWindowListener() {
            
            public void windowActivated(IWorkbenchWindow window) {
            }
            
            public void windowClosed(IWorkbenchWindow closedWindow) {
                RailsWindow railsWindow = mapping.remove(closedWindow);
                if (PlatformUI.getWorkbench().isClosing())
                    // windows closed when exitting the application should not cause
                    // projects to be deleted
                    return;
                if (railsWindow != null) {
                    RailsProject railsProject = railsWindow.getRailsProject();
                    if (railsProject != null) {
                        boolean anyWindowsRemain = false;
                        IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
                        for (IWorkbenchWindow window : windows) {
                            if (window == closedWindow)
                                continue;
                            RailsProject windowRailsProject = getWindow(window).getRailsProject();
                            if (windowRailsProject == railsProject) {
                                anyWindowsRemain = true;
                                break;
                            }
                        }
                        if (!anyWindowsRemain) {
                            YourSwayProjects.closeProject(railsProject);
                        }
                    }
                }
            }
            
            public void windowDeactivated(IWorkbenchWindow window) {
            }
            
            public void windowOpened(IWorkbenchWindow window) {
            }
            
        });
    }
    
    public void addListener(IRailsWindowModelListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(IRailsWindowModelListener listener) {
        listeners.remove(listener);
    }
    
    public RailsWindow getWindow(IWorkbenchWindow window) {
        RailsWindow railsWindow = mapping.get(window);
        if (railsWindow == null) {
            railsWindow = new RailsWindow(this, window);
            mapping.put(window, railsWindow);
        }
        return railsWindow;
    }
    
    void fire(RailsWindowModelChange event) {
        for (IRailsWindowModelListener listener : listeners) {
            event.sendTo(listener);
        }
    }
    
    public Collection<RailsWindow> findWindows(RailsProject railsProject) {
        Collection<RailsWindow> result = new ArrayList<RailsWindow>();
        for (RailsWindow window : mapping.values())
            if (window.getRailsProject() == railsProject)
                result.add(window);
        return result;
    }
    
    public void replaceProject(RailsProject oldRailsProject, RailsProject railsProject) {
        for (RailsWindow window : mapping.values())
            if (window.getRailsProject() == oldRailsProject)
                window.setRailsProject(railsProject);
    }
    
}
