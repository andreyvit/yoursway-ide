package com.yoursway.ide.projects.editor.feedback;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IMessageManager;

public class ControlValidationFeedback implements ValidationFeedback {
    
    private final IMessageManager messageManager;
    private final Control control;
    private final Binding binding;
    
    public ControlValidationFeedback(IMessageManager messageManager, Binding binding, Control control) {
        this.messageManager = messageManager;
        this.binding = binding;
        this.control = control;
    }
    
    public void setFeedback(IStatus status) {
        if (status.getSeverity() != IStatus.OK) {
            int severity = MessageManagerFeedback.mapSeverity(status.getSeverity());
            messageManager.addMessage(binding, status.getMessage(), null, severity, control);
        } else
            messageManager.removeMessage(binding, control);
    }
    
}
