package com.yoursway.ruby.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;

public class RubyFactory {
    
    private static final RubyFactory INSTANCE = new RubyFactory();
    
    public static RubyFactory instance() {
        return INSTANCE;
    }
    
    public RubyMethod createMethod(IMethod method) {
        return new RubyMethod(method);
    }
    
    public RubyFile createFile(ISourceModule sourceModule) {
        return new RubyFile(sourceModule);
    }
    
    public RubyFile createFile(org.eclipse.dltk.compiler.env.ISourceModule sourceModule) {
        return createFile((ISourceModule) sourceModule);
    }
    
    public RubyFile createFile(IFile file) {
        IModelElement modelElement = DLTKCore.create(file);
        if (modelElement != null && modelElement.getElementType() == IModelElement.SOURCE_MODULE)
            return createFile((ISourceModule) modelElement);
        else
            return null;
    }
    
}
