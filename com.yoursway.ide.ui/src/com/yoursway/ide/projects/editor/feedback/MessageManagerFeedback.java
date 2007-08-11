package com.yoursway.ide.projects.editor.feedback;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IMessageManager;

public class MessageManagerFeedback {
    
    private final IMessageManager messageManager;
    
    private static final class FeedbackUpdater implements IValueChangeListener {
        private final IObservableValue validationStatus;
        private final ValidationFeedback feedback;
        
        private FeedbackUpdater(IObservableValue validationStatus, ValidationFeedback feedback) {
            this.validationStatus = validationStatus;
            this.feedback = feedback;
        }
        
        public void handleValueChange(ValueChangeEvent event) {
            feedback.setFeedback((IStatus) validationStatus.getValue());
        }
    }
    
    static final class Connector implements IFeedbackConnector {
        
        private final IMessageManager messageManager;
        private final Binding binding;
        
        private ValidationFeedback feedback;
        
        public Connector(IMessageManager messageManager, Binding binding) {
            this.messageManager = messageManager;
            this.binding = binding;
        }
        
        public void bindTo(Control control) {
            feedback = new ControlValidationFeedback(messageManager, binding, control);
        }
        
        public ValidationFeedback getFeedback() {
            if (feedback == null)
                feedback = new DefaultValidationFeedback(messageManager, binding);
            return feedback;
        }
        
    }
    
    public MessageManagerFeedback(IMessageManager messageManager) {
        this.messageManager = messageManager;
    }
    
    public void hook(Binding binding, IFeedbackTarget feedbackTarget) {
        binding.getValidationStatus().addValueChangeListener(
                new FeedbackUpdater(binding.getValidationStatus(), connect(binding, feedbackTarget)));
    }
    
    private ValidationFeedback connect(Binding binding, IFeedbackTarget feedbackTarget) {
        Connector connector = new Connector(messageManager, binding);
        feedbackTarget.connect(connector);
        return connector.getFeedback();
    }
    
    static int mapSeverity(int severity) {
        switch (severity) {
        case IStatus.ERROR:
            return IMessageProvider.ERROR;
        case IStatus.WARNING:
            return IMessageProvider.WARNING;
        case IStatus.INFO:
            return IMessageProvider.INFORMATION;
        default:
            throw new AssertionError("Improper severity for translation: " + severity);
        }
    }
    
}
