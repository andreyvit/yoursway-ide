/**
 * 
 */
package com.yoursway.ruby.wala.translator;

import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;

import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.yoursway.rails.commons.RubyAstVisitor;
import com.yoursway.ruby.wala.entities.ScriptEntity;

class ModuleState extends AbstractModuleOrFuncState<ModuleDeclaration> {
    
    public ModuleState(RubyAstVisitor<?> parentVisitor) {
        super(parentVisitor);
    }
    
    protected void createEntity(Map<CAstNode, Set<CAstEntity>> subs, CAstNode ast) {
        getParentVisitor().addChildEntity(new ScriptEntity("test", ast, subs, pos(), cfg()));
    }
}