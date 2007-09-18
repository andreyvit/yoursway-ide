package com.yoursway.ruby.wala2.entities;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.ast.declarations.TypeDeclaration;

import com.ibm.wala.cast.tree.CAstControlFlowMap;
import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.ibm.wala.cast.tree.CAstNodeTypeMap;
import com.ibm.wala.cast.tree.CAstSourcePositionMap;
import com.ibm.wala.cast.tree.CAstType;
import com.ibm.wala.cast.tree.CAstSourcePositionMap.Position;
import com.ibm.wala.util.collections.EmptyIterator;

public class ClassEntity implements CAstEntity {

	private final CAstNode ast;
	private final Map<CAstNode, Collection<CAstEntity>> subentities;
	private CAstSourcePositionMap sourcePositionMap;
	private CAstControlFlowMap controlFlowMap;
	private final TypeDeclaration decl;

	public ClassEntity(CAstNode ast,
			Map<CAstNode, Set<CAstEntity>> subentities,
			CAstSourcePositionMap sourcePositionMap,
			CAstControlFlowMap controlFlowMap, TypeDeclaration decl) {
		this.decl = decl;
		this.ast = ast;
		this.sourcePositionMap = sourcePositionMap;
		this.controlFlowMap = controlFlowMap;
		this.subentities = new HashMap<CAstNode, Collection<CAstEntity>>(
				subentities);
	}

	public CAstNode getAST() {
		return ast;
	}

	public Map<CAstNode, Collection<CAstEntity>> getAllScopedEntities() {
		return Collections.unmodifiableMap(subentities);
	}

	public int getArgumentCount() {
		System.out.println("MethodEntity.getArgumentCount()");
		return 0;
	}

	public CAstNode[] getArgumentDefaults() {
		System.out.println("MethodEntity.getArgumentDefaults()");
		return new CAstNode[0];
	}

	public String[] getArgumentNames() {
		return new String[0];
	}

	public CAstControlFlowMap getControlFlow() {
		return controlFlowMap;
	}

	public int getKind() {
		return CAstEntity.FUNCTION_ENTITY;
	}

	public String getName() {
		return decl.getName();
	}

	public CAstNodeTypeMap getNodeTypeMap() {
		throw new UnsupportedOperationException();
	}

	public Position getPosition() {
		throw new UnsupportedOperationException();
	}

	public Collection getQualifiers() {
		throw new UnsupportedOperationException();
	}

	public Iterator<CAstEntity> getScopedEntities(CAstNode construct) {
		if (subentities.containsKey(construct))
			return ((Set<CAstEntity>) subentities.get(construct)).iterator();
		else
			return EmptyIterator.instance();
	}

	public String getSignature() {
		throw new UnsupportedOperationException();
	}

	public CAstSourcePositionMap getSourceMap() {
		return sourcePositionMap;
	}

	public CAstType getType() {
		throw new UnsupportedOperationException();
	}

}
