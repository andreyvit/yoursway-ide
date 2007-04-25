package com.yoursway.rails.windowmodel;

import org.eclipse.ui.IWorkbenchWindow;

import com.yoursway.rails.model.IRailsProject;

public class RailsWindow {
    
    private final RailsWindowModel windowModel;
    
    private IRailsProject activeProject;
    
    private INonModalMode activeMode;
    
    private final IWorkbenchWindow window;
    
    public RailsWindow(RailsWindowModel model, IWorkbenchWindow window) {
        this.windowModel = model;
        this.window = window;
    }
    
    public IWorkbenchWindow getWindow() {
        return window;
    }
    
    public IRailsProject getRailsProject() {
        return activeProject;
    }
    
    public void setRailsProject(IRailsProject newProject) {
        IRailsProject oldProject = activeProject;
        activeProject = newProject;
        windowModel.fire(new RailsWindowModelProjectChange(window, oldProject, newProject));
    }
    
    public INonModalMode getActiveMode() {
        return activeMode;
    }
    
    public synchronized void modeDeactivated(INonModalMode mode) {
        if (activeMode != mode)
            return;
        activeMode = null;
        windowModel.fire(new RailsWindowModelModeChange(window, mode, null));
    }
    
    public synchronized void activateMode(INonModalMode newMode) {
        INonModalMode oldMode = activeMode;
        if (oldMode != null) {
            activeMode = null; // XXX a buggy piece of code
            oldMode.leave();
        }
        activeMode = newMode;
        windowModel.fire(new RailsWindowModelModeChange(window, oldMode, activeMode));
    }
    
    public RailsWindowModel getWindowModel() {
        return windowModel;
    }
    
}
