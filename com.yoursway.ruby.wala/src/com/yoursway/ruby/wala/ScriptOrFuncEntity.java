/**
 * 
 */
package com.yoursway.ruby.wala;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.mozilla.javascript.FunctionNode;
import org.mozilla.javascript.ScriptOrFnNode;

import com.ibm.wala.cast.tree.CAstControlFlowMap;
import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.ibm.wala.cast.tree.CAstNodeTypeMap;
import com.ibm.wala.cast.tree.CAstSourcePositionMap;
import com.ibm.wala.cast.tree.CAstType;
import com.ibm.wala.util.collections.EmptyIterator;
import com.ibm.wala.util.debug.Assertions;

/**
 * This class is for comparison purposes only and will be deleted soon.
 * 
 * @author Andrey Tarantsov
 */
final class ScriptOrFuncEntity implements CAstEntity {
    private final Map<CAstNode, Collection<CAstEntity>> subs;
    private final CAstNode ast;
    private final ScriptOrFnNode n;
    private final CAstSourcePositionMap pos;
    private final CAstControlFlowMap map;
    private final String[] arguments;
    private final String name;
    
    // constructor of inner class
    
    ScriptOrFuncEntity(Map<CAstNode, Collection<CAstEntity>> subs, CAstNode ast, ScriptOrFnNode n,
            CAstSourcePositionMap pos, CAstControlFlowMap map) {
        this.subs = subs;
        this.ast = ast;
        this.n = n;
        this.pos = pos;
        this.map = map;
        
        if (n instanceof FunctionNode) {
            String x = ((FunctionNode) n).getFunctionName();
            if (x == null || "".equals(x)) {
                name = dltkAstToCommonAstTranslator.scriptName + "_anonymous_"
                        + dltkAstToCommonAstTranslator.anonymousCounter++;
            } else {
                name = x;
            }
        } else {
            name = n.getSourceName();
        }
        
        if (n instanceof FunctionNode) {
            FunctionNode f = (FunctionNode) n;
            int i = 0;
            arguments = new String[f.getParamCount() + 2];
            arguments[i++] = name;
            arguments[i++] = "this";
            for (int j = 0; j < f.getParamCount(); j++) {
                arguments[i++] = f.getParamOrVarName(j);
            }
        } else {
            arguments = new String[0];
        }
    }
    
    public String toString() {
        return "<JS function " + getName() + ">";
    }
    
    public String getName() {
        return name;
    }
    
    public String getSignature() {
        Assertions.UNREACHABLE();
        return null;
    }
    
    public int getKind() {
        if (n instanceof FunctionNode)
            return CAstEntity.FUNCTION_ENTITY;
        else
            return CAstEntity.SCRIPT_ENTITY;
    }
    
    public String[] getArgumentNames() {
        return arguments;
    }
    
    public CAstNode[] getArgumentDefaults() {
        return new CAstNode[0];
    }
    
    public int getArgumentCount() {
        return arguments.length;
    }
    
    public Map<CAstNode, Collection<CAstEntity>> getAllScopedEntities() {
        return Collections.unmodifiableMap(subs);
    }
    
    public Iterator getScopedEntities(CAstNode construct) {
        if (subs.containsKey(construct))
            return ((Set) subs.get(construct)).iterator();
        else
            return EmptyIterator.instance();
    }
    
    public CAstNode getAST() {
        return ast;
    }
    
    public CAstControlFlowMap getControlFlow() {
        return map;
    }
    
    public CAstSourcePositionMap getSourceMap() {
        return pos;
    }
    
    public CAstSourcePositionMap.Position getPosition() {
        return null;
    }
    
    public CAstNodeTypeMap getNodeTypeMap() {
        return null;
    }
    
    public Collection getQualifiers() {
        Assertions.UNREACHABLE("JuliansUnnamedCAstEntity$2.getQualifiers()");
        return null;
    }
    
    public CAstType getType() {
        Assertions.UNREACHABLE("JuliansUnnamedCAstEntity$2.getType()");
        return null;
    }
}