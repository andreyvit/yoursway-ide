package com.yoursway.ide.analysis.model;

import org.eclipse.dltk.core.ElementChangedEvent;
import org.eclipse.dltk.core.IModelElementDelta;

public interface IAdviceProvider extends IIteratesAdvices {
    
    void setListener(IAdviceProviderListener listener);
    
    void processModelChange(ElementChangedEvent event, IModelElementDelta delta);
    
    void dispose();
    
}
