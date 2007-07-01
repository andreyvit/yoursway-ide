package com.yoursway.ide.windowing;

import org.eclipse.ui.IWorkbenchWindow;

public class RailsWindowModelModeChange extends RailsWindowModelChange {
    
    private final INonModalMode oldMode;
    private final INonModalMode newMode;
    
    public RailsWindowModelModeChange(IWorkbenchWindow window, INonModalMode oldMode, INonModalMode newMode) {
        super(window);
        this.oldMode = oldMode;
        this.newMode = newMode;
    }
    
    public INonModalMode getOldMode() {
        return oldMode;
    }
    
    public INonModalMode getNewMode() {
        return newMode;
    }
    
    @Override
    void sendTo(IRailsWindowModelListener listener) {
        listener.activeModeChanged(this);
    }
    
}
