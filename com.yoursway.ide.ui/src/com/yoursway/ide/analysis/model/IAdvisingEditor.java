package com.yoursway.ide.analysis.model;

import org.eclipse.dltk.core.ISourceModule;

public interface IAdvisingEditor {
    
    ISourceModule getSourceModule();
    
    void advicesChanged(IAdvicesChangeEvent event);
    
}
