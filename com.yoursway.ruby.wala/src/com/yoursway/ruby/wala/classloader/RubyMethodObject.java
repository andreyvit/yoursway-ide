/**
 * 
 */
package com.yoursway.ruby.wala.classloader;

import com.ibm.wala.cast.loader.AstMethod;
import com.ibm.wala.cast.types.AstMethodReference;
import com.ibm.wala.cfg.AbstractCFG;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ssa.SymbolTable;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.debug.Trace;
import com.yoursway.ruby.wala.RubyTypes;

public class RubyMethodObject extends AstMethod {
    
    /**
     * 
     */
    private final RubyClassLoader rubyClassLoader;

    RubyMethodObject(RubyClassLoader rubyClassLoader, RubyCodeBody cls, AbstractCFG cfg, SymbolTable symtab,
            boolean hasCatchBlock, TypeReference[][] caughtTypes, LexicalInformation lexicalInfo,
            DebuggingInformation debugInfo) {
        super(cls, rubyClassLoader.functionQualifiers, cfg, symtab, AstMethodReference.fnReference(cls.getReference()),
                hasCatchBlock, caughtTypes, lexicalInfo, debugInfo);
        this.rubyClassLoader = rubyClassLoader;
    }
    
    public IClassHierarchy getClassHierarchy() {
        return this.rubyClassLoader.cha;
    }
    
    public String toString() {
        return "<Code body of " + cls + ">";
    }
    
    public TypeReference[] getDeclaredExceptions() {
        return null;
    }
    
    public LexicalParent[] getParents() {
        if (lexicalInfo == null)
            return new LexicalParent[0];
        
        final String[] parents = lexicalInfo.getScopingParents();
        
        if (parents == null)
            return new LexicalParent[0];
        
        LexicalParent result[] = new LexicalParent[parents.length];
        
        for (int i = 0; i < parents.length; i++) {
            final int hack = i;
            final AstMethod method = (AstMethod) this.rubyClassLoader.lookupClass(parents[i], this.rubyClassLoader.cha).getMethod(
                    AstMethodReference.fnSelector);
            result[i] = new LexicalParent() {
                public String getName() {
                    return parents[hack];
                }
                
                public AstMethod getMethod() {
                    return method;
                }
            };
            
            Trace.println("parent " + result[i].getName() + " is " + result[i].getMethod());
        }
        
        return result;
    }
    
    public String getLocalVariableName(int bcIndex, int localNumber) {
        return null;
    }
    
    public boolean hasLocalVariableTable() {
        return false;
    }
    
    public int getMaxLocals() {
        Assertions.UNREACHABLE();
        return -1;
    }
    
    public int getMaxStackHeight() {
        Assertions.UNREACHABLE();
        return -1;
    }
    
    public TypeReference getParameterType(int i) {
        return RubyTypes.Root;
    }
}