package com.yoursway.model.rails;

import java.util.Collection;

import com.yoursway.model.rails.conventionalClassNames.IConventionalClassName;
import com.yoursway.model.repository.IHandle;

public interface IRailsController extends IFileBasedElement {
    
    IHandle<IConventionalClassName> name();
    
    IHandle<Collection<IRailsView>> views();
    
    IHandle<Collection<IRailsControllerAction>> actions();
    
    IHandle<Collection<IRailsPartial>> partials();
    
}
