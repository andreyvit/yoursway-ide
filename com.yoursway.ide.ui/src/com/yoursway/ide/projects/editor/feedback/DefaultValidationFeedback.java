package com.yoursway.ide.projects.editor.feedback;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.forms.IMessageManager;

public class DefaultValidationFeedback implements ValidationFeedback {
    
    private final IMessageManager messageManager;
    private final Binding binding;
    
    public DefaultValidationFeedback(IMessageManager messageManager, Binding binding) {
        this.messageManager = messageManager;
        this.binding = binding;
    }
    
    public void setFeedback(IStatus status) {
        if (status.getSeverity() != IStatus.OK) {
            int severity = MessageManagerFeedback.mapSeverity(status.getSeverity());
            messageManager.addMessage(binding, status.getMessage(), null, severity);
        } else
            messageManager.removeMessage(binding);
    }
    
}
