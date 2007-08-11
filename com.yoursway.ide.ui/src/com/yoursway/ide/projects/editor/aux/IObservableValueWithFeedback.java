package com.yoursway.ide.projects.editor.aux;

import org.eclipse.core.databinding.observable.value.IObservableValue;

import com.yoursway.ide.projects.editor.feedback.IFeedbackTarget;

public interface IObservableValueWithFeedback {
    
    IObservableValue observe();
    
    IFeedbackTarget feedback();
    
}
