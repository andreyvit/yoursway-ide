package com.yoursway.ide.analysis.model;

import org.eclipse.dltk.core.ISourceModule;

public interface IAdviceProviderFactory {
    
    void createProviders(ISourceModule sourceModule, IAdviceProviderRequestor requestor);
    
}
