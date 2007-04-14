package com.yoursway.rails.model.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.model.IRailsAction;
import com.yoursway.rails.model.IRailsControllerActionsCollection;
import com.yoursway.ruby.model.RubyMethod;

public class RailsAction implements IRailsAction {
    
    private final IRailsControllerActionsCollection parent;
    private final RubyMethod method;
    
    public RailsAction(IRailsControllerActionsCollection parent, RubyMethod rubyMethod) {
        Assert.isLegal(parent != null);
        Assert.isLegal(rubyMethod != null);
        
        this.parent = parent;
        this.method = rubyMethod;
    }
    
    public RubyMethod getMethod() {
        return method;
    }
    
    public String getName() {
        return method.getName();
    }
    
    public IFile[] getViews() {
        return null;
    }
    
}
