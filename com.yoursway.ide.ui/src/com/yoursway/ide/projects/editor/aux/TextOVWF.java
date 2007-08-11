package com.yoursway.ide.projects.editor.aux;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.yoursway.ide.projects.editor.feedback.ControlFeedbackTarget;
import com.yoursway.ide.projects.editor.feedback.IFeedbackTarget;

public class TextOVWF implements IObservableValueWithFeedback {
    
    private final Text control;
    
    public TextOVWF(Text control) {
        this.control = control;
    }
    
    public IFeedbackTarget feedback() {
        return new ControlFeedbackTarget(control);
    }
    
    public IObservableValue observe() {
        return SWTObservables.observeText(control, SWT.Modify);
    }
    
}
