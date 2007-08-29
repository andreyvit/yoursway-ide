/**
 * 
 */
package com.yoursway.ruby.wala.translator;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.CallExpression;

import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.yoursway.rails.commons.RubyAstVisitor;

class RootState extends AbstractState<ASTNode> {
    
    private CAstEntity moduleEntity;
    
    public RootState(Context context) {
        super(null, context);
    }
    
    @Override
    protected RubyAstVisitor<?> enterModuleDeclaration(ModuleDeclaration node) {
        return new ModuleState(this);
    }
    
    protected void addChildNode(CAstNode node) {
        
    }
    
    protected void addChildEntity(CAstEntity entity) {
        this.moduleEntity = entity;
    }
    
    public CAstEntity getModuleEntity() {
        return moduleEntity;
    }
     
}