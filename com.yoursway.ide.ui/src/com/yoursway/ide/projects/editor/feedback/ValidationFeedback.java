package com.yoursway.ide.projects.editor.feedback;

import org.eclipse.core.runtime.IStatus;

public interface ValidationFeedback {
    
    void setFeedback(IStatus status);
    
}
