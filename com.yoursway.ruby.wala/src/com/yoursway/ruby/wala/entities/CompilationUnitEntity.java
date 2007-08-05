package com.yoursway.ruby.wala.entities;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ibm.wala.cast.tree.CAstControlFlowMap;
import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.ibm.wala.cast.tree.CAstNodeTypeMap;
import com.ibm.wala.cast.tree.CAstQualifier;
import com.ibm.wala.cast.tree.CAstSourcePositionMap;
import com.ibm.wala.cast.tree.CAstType;
import com.ibm.wala.util.debug.Assertions;

public final class CompilationUnitEntity implements CAstEntity {
    private final String fName;
    
    private final Collection<CAstEntity> fTopLevelDecls;
    
    public CompilationUnitEntity(String name, List<CAstEntity> topLevelDecls) {
        fName = name;
        fTopLevelDecls = topLevelDecls;
    }
    
    public int getKind() {
        return FILE_ENTITY;
    }
    
    public String getName() {
        return fName;
    }
    
    public String getSignature() {
        Assertions.UNREACHABLE();
        return null;
    }
    
    public String[] getArgumentNames() {
        return new String[0];
    }
    
    public CAstNode[] getArgumentDefaults() {
        return new CAstNode[0];
    }
    
    public int getArgumentCount() {
        return 0;
    }
    
    public Map<CAstNode, Collection<CAstEntity>> getAllScopedEntities() {
        return Collections.singletonMap(null, fTopLevelDecls);
    }
    
    @SuppressWarnings("unchecked")
    public Iterator<CAstEntity> getScopedEntities(CAstNode construct) {
        throw new AssertionError("CompilationUnitEntity asked for AST-related entities, but it has no AST.");
    }
    
    public CAstNode getAST() {
        return null;
    }
    
    public CAstControlFlowMap getControlFlow() {
        throw new AssertionError("CompilationUnitEntity.getControlFlow()");
    }
    
    public CAstSourcePositionMap getSourceMap() {
        throw new AssertionError("CompilationUnitEntity.getSourceMap()");
    }
    
    public CAstSourcePositionMap.Position getPosition() {
        throw new AssertionError("CompilationUnitEntity.getPosition()");
    }
    
    public CAstNodeTypeMap getNodeTypeMap() {
        throw new AssertionError("CompilationUnitEntity.getNodeTypeMap()");
    }
    
    public Collection<CAstQualifier> getQualifiers() {
        return Collections.emptyList();
    }
    
    public CAstType getType() {
        throw new AssertionError("CompilationUnitEntity.getType()");
    }
}
