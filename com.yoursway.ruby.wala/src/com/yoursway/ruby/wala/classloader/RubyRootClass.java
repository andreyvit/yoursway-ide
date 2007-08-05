/**
 * 
 */
package com.yoursway.ruby.wala.classloader;

import java.util.Collection;
import java.util.Collections;

import com.ibm.wala.cast.loader.AstDynamicPropertyClass;
import com.ibm.wala.cast.tree.CAstSourcePositionMap;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IClassLoader;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.yoursway.ruby.wala.RubyTypes;

class RubyRootClass extends AstDynamicPropertyClass {
    
    /**
     * 
     */
    private final RubyClassLoader rubyClassLoader;

    RubyRootClass(RubyClassLoader rubyClassLoader, IClassLoader loader, CAstSourcePositionMap.Position sourcePosition) {
        super(sourcePosition, RubyTypes.Root.getName(), loader, (short) 0, RubyClassLoader.emptyMap1,
                RubyTypes.Root);
        this.rubyClassLoader = rubyClassLoader;
        
        this.rubyClassLoader.types.put(RubyTypes.Root.getName(), this);
    }
    
    public IClassHierarchy getClassHierarchy() {
        return this.rubyClassLoader.cha;
    }
    
    public String toString() {
        return "JS Root:" + getReference().toString();
    }
    
    public Collection<IClass> getDirectInterfaces() throws ClassHierarchyException {
        return Collections.emptySet();
    }
    
    public IClass getSuperclass() throws ClassHierarchyException {
        return null;
    }
}