package com.yoursway.ruby.wala2.translator;

import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.ast.declarations.TypeDeclaration;

import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.yoursway.rails.commons.RubyAstVisitor;
import com.yoursway.ruby.wala2.entities.ClassEntity;

public class ClassState extends
		AbstractModuleOrFuncOrClassState<TypeDeclaration> {

	public ClassState(RubyAstVisitor<?> parentVisitor) {
		super(parentVisitor);
	}

	@Override
	protected void createEntity(Map<CAstNode, Set<CAstEntity>> subs,
			CAstNode ast) {
		getParentVisitor().addChildEntity(
				new ClassEntity(ast, subs, pos(), cfg(), node()));
	}

}
