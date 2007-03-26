package com.yoursway.ide.analysis.model;

import org.eclipse.dltk.core.ISourceModule;

public interface IAdvisoryManager {
    
    void addAdviceProviderFactory(IAdviceProviderFactory factory);
    
    IIteratesAdvices registerEditor(IAdvisingEditor editor);
    
    void unregisterEditor(IAdvisingEditor editor);
    
    void sourceModuleChanged(ISourceModule sourceModule);
    
}
