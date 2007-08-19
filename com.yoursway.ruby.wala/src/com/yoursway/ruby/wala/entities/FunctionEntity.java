/**
 * 
 */
package com.yoursway.ruby.wala.entities;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ibm.wala.cast.tree.CAstControlFlowMap;
import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.ibm.wala.cast.tree.CAstNodeTypeMap;
import com.ibm.wala.cast.tree.CAstSourcePositionMap;
import com.ibm.wala.cast.tree.CAstType;
import com.ibm.wala.util.collections.EmptyIterator;
import com.ibm.wala.util.debug.Assertions;

/**
 * Used to represent a JavaScript function, until it has been adapted for Ruby.
 * Now probably represents a method, but no one is sure. ;)
 * 
 * @author Andrey Tarantsov
 */
public final class FunctionEntity implements CAstEntity {
    private final String[] arguments;
    private final String name;
    private final CAstNode ast;
    
    private final Map<CAstNode, Collection<CAstEntity>> subentities;
    private CAstSourcePositionMap sourcePositionMap;
    private CAstControlFlowMap controlFlowMap;
    
    public FunctionEntity(String name, CAstNode ast, Map<CAstNode, Set<CAstEntity>> subentities,
            CAstSourcePositionMap sourcePositionMap, CAstControlFlowMap controlFlowMap) {
        this.name = name;
        this.ast = ast;
        this.sourcePositionMap = sourcePositionMap;
        this.controlFlowMap = controlFlowMap;
        this.subentities = new HashMap<CAstNode, Collection<CAstEntity>>(subentities);
        arguments = new String[0]; // FIXME: initialize with args from AST node
    }
    
    public String toString() {
        return "<Ruby function " + getName() + ">";
    }
    
    public String getName() {
        return name;
    }
    
    public String getSignature() {
        Assertions.UNREACHABLE();
        return null;
    }
    
    public int getKind() {
        return CAstEntity.FUNCTION_ENTITY;
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
    
    public CAstNode getAST() {
        return ast;
    }
    
    public Map<CAstNode, Collection<CAstEntity>> getAllScopedEntities() {
        return Collections.unmodifiableMap(subentities);
    }
    
    public Iterator<CAstEntity> getScopedEntities(CAstNode construct) {
        if (subentities.containsKey(construct))
            return ((Set<CAstEntity>) subentities.get(construct)).iterator();
        else
            return EmptyIterator.instance();
    }
    
    public CAstControlFlowMap getControlFlow() {
        return controlFlowMap;
    }
    
    public CAstSourcePositionMap getSourceMap() {
        return sourcePositionMap;
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