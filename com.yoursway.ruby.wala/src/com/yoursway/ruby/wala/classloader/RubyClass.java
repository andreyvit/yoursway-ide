/**
 * 
 */
package com.yoursway.ruby.wala.classloader;

import java.util.Collection;
import java.util.Collections;

import com.ibm.wala.cast.loader.AstClass;
import com.ibm.wala.cast.tree.CAstSourcePositionMap;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.TypeReference;

class RubyClass extends AstClass {
    /**
     * 
     */
    private final RubyClassLoader loader;
    private IClass superClass;
    
    RubyClass(RubyClassLoader loader, TypeReference classRef, TypeReference superRef, CAstSourcePositionMap.Position sourcePosition) {
        super(sourcePosition, classRef.getName(), loader, (short) 0, RubyClassLoader.emptyMap2, RubyClassLoader.emptyMap1);
        this.loader = loader;
        this.loader.types.put(classRef.getName(), this);
        superClass = superRef == null ? null : loader.lookupClass(superRef.getName());
    }
    
    public IClassHierarchy getClassHierarchy() {
        return this.loader.cha;
    }
    
    public String toString() {
        return "JS:" + getReference().toString();
    }
    
    @Override
    public Collection<IClass> getDirectInterfaces() throws ClassHierarchyException {
        return Collections.emptySet();
    }
    
    @Override
    public IClass getSuperclass() throws ClassHierarchyException {
        return superClass;
    }
}