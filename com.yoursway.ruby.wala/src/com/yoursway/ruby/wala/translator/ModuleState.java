/**
 * 
 */
package com.yoursway.ruby.wala.translator;

import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;

import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.yoursway.rails.commons.RubyAstVisitor;
import com.yoursway.ruby.wala.entities.ScriptEntity;

class ModuleState extends AbstractModuleOrFuncState<ModuleDeclaration> {
    
    public ModuleState(RubyAstVisitor<?> parentVisitor) {
        super(parentVisitor);
    }
    
    @Override
    protected RubyAstVisitor<?> enterMethodDeclaration(MethodDeclaration node) {
        return new MethodState(this);
    }

    protected void createEntity(Map<CAstNode, Set<CAstEntity>> subs, CAstNode ast) {
        CAstNode functionDef = astBuilder().makeNode(CAstNode.ASSIGN, astBuilder().makeNode(CAstNode.VAR, astBuilder().makeConstant("Function")),
                astBuilder().makeNode(CAstNode.PRIMITIVE, astBuilder().makeConstant("NewFunction")));
        CAstNode newAst = astBuilder().makeNode(CAstNode.BLOCK_STMT, functionDef, ast);
        getParentVisitor().addChildEntity(new ScriptEntity("test", newAst, subs, pos(), cfg()));
    }
}