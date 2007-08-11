package com.yoursway.ide.projects.editor.feedback;

import org.eclipse.swt.widgets.Control;

public class ControlFeedbackTarget implements IFeedbackTarget {
    
    private final Control control;
    
    public ControlFeedbackTarget(Control control) {
        this.control = control;
    }
    
    public void connect(IFeedbackConnector connector) {
        connector.bindTo(control);
    }
    
}
