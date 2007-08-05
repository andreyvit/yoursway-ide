/**
 * 
 */
package com.yoursway.ruby.wala.classloader;

import com.ibm.wala.cast.loader.AstFunctionClass;
import com.ibm.wala.cast.tree.CAstSourcePositionMap;
import com.ibm.wala.classLoader.IClassLoader;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.TypeReference;

class RubyCodeBody extends AstFunctionClass {
    
    /**
     * 
     */
    private final RubyClassLoader rubyClassLoader;

    public RubyCodeBody(RubyClassLoader rubyClassLoader, TypeReference codeName, TypeReference parent, IClassLoader loader,
            CAstSourcePositionMap.Position sourcePosition) {
        super(codeName, parent, loader, sourcePosition);
        this.rubyClassLoader = rubyClassLoader;
        this.rubyClassLoader.types.put(codeName.getName(), this);
    }
    
    public IClassHierarchy getClassHierarchy() {
        return this.rubyClassLoader.cha;
    }
    
    IMethod setCodeBody(IMethod codeBody) {
        this.functionBody = codeBody;
        return codeBody;
    }
}