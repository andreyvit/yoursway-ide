package com.yoursway.ruby.model;

import org.eclipse.dltk.core.IMethod;

public class RubyFactory {
    
    private static final RubyFactory INSTANCE = new RubyFactory();
    
    public static RubyFactory instance() {
        return INSTANCE;
    }
    
    public RubyMethod createMethod(IMethod method) {
        return new RubyMethod(method);
    }
    
}
