package com.yoursway.model.rails;

import com.yoursway.model.rails.conventionalClassNames.IConventionalClassName;
import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IHandle;

public interface IRailsController extends IFileBasedElement {
    
    IHandle<IConventionalClassName> name();
    
    ICollectionHandle<IRailsView> views();
    
    ICollectionHandle<IRailsControllerAction> actions();
    
    ICollectionHandle<IRailsPartial> partials();
    
}
