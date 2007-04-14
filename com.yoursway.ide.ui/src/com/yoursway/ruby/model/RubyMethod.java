package com.yoursway.ruby.model;

import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.ModelException;

public class RubyMethod {
    
    private final IMethod dltkMethod;
    
    public RubyMethod(IMethod method) {
        this.dltkMethod = method;
    }
    
    public String getName() {
        return dltkMethod.getElementName();
    }
    
    public IMethod getDLTKMethod() {
        return dltkMethod;
    }
    
    public boolean isPublic() {
        int flags;
        try {
            flags = dltkMethod.getFlags();
        } catch (ModelException e) {
            throw new DLTKFailure(e);
        }
        return 0 == (flags & (Modifiers.AccPrivate | Modifiers.AccProtected));
    }
    
}
