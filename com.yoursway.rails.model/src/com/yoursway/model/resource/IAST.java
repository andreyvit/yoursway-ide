package com.yoursway.model.resource;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;

public interface IAST {
    
    String getTopLevelClassName();
    
    ModuleDeclaration getModuleDeclaration();
    
}
