package com.yoursway.model.rails;

import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelElement;

public interface IRailsController extends IFileBasedElement, IModelElement {
    
    IHandle<String> getName();
    
    ICollectionHandle<IRailsView> getViews();
    
    ICollectionHandle<IRailsControllerAction> getActions();
    
    ICollectionHandle<IRailsPartial> getPartials();
    
}
