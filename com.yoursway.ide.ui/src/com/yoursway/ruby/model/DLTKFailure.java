package com.yoursway.ruby.model;

import org.eclipse.dltk.core.ModelException;

public class DLTKFailure extends RubyModelFailure {
    
    private static final long serialVersionUID = -4729409247000545567L;
    
    private final ModelException modelException;
    
    public DLTKFailure(ModelException modelException) {
        this.modelException = modelException;
    }
    
}
