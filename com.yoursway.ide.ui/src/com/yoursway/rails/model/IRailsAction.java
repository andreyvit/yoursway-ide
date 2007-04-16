package com.yoursway.rails.model;

import org.eclipse.core.resources.IFile;

import com.yoursway.ruby.model.RubyMethod;

public interface IRailsAction extends IRailsElement {
    
    String getName();
    
    RubyMethod getMethod();
    
    IFile[] getViews();
    
    //    Collection<? extends IRailsBaseView> getRenderedViews();
    
}
