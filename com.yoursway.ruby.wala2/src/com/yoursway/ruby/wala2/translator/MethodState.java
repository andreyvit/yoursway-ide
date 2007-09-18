/**
 * 
 */
package com.yoursway.ruby.wala2.translator;

import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.ast.declarations.MethodDeclaration;

import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.yoursway.rails.commons.RubyAstVisitor;
import com.yoursway.ruby.wala2.entities.MethodEntity;

class MethodState extends AbstractModuleOrFuncOrClassState<MethodDeclaration> {
    
    public MethodState(RubyAstVisitor<?> parentVisitor) {
        super(parentVisitor);
    }
    
    protected void createEntity(Map<CAstNode, Set<CAstEntity>> subs, CAstNode ast) {
        getParentVisitor().defineMethod(new MethodEntity(ast, subs, pos(), cfg(), node()));
    }
    
}