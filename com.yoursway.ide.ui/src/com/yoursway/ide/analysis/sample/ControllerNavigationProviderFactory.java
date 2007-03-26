package com.yoursway.ide.analysis.sample;

import org.eclipse.dltk.core.ISourceModule;

import com.yoursway.ide.analysis.model.AdvisoryManager;
import com.yoursway.ide.analysis.model.IAdviceProviderFactory;
import com.yoursway.ide.analysis.model.IAdviceProviderRequestor;

public class ControllerNavigationProviderFactory implements IAdviceProviderFactory {
    
    private static ControllerNavigationProviderFactory INSTANCE;
    
    public void createProviders(ISourceModule sourceModule, IAdviceProviderRequestor requestor) {
        requestor.acceptProvider(new ControllerNavigationProvider(sourceModule));
    }
    
    public static void initialize() {
        if (INSTANCE == null) {
            INSTANCE = new ControllerNavigationProviderFactory();
            AdvisoryManager.instance().addAdviceProviderFactory(INSTANCE);
        }
    }
    
}
