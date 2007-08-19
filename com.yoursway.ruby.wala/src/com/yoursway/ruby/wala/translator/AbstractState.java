/**
 * 
 */
package com.yoursway.ruby.wala.translator;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;

import com.ibm.wala.cast.tree.CAst;
import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.yoursway.rails.commons.RubyAstVisitor;
import com.yoursway.ruby.wala.entities.FunctionEntity;

abstract class AbstractState<T extends ASTNode> extends RubyAstVisitor<T> {
    
    private final Context context;
    
    public AbstractState(RubyAstVisitor<?> parentVisitor) {
        super(parentVisitor);
        context = getParentVisitor().context;
    }
    
    public AbstractState(RubyAstVisitor<?> parentVisitor, Context context) {
        super(parentVisitor);
        this.context = context;
    }
    
    protected void addChildNode(CAstNode node) {
        
    }
    
    protected void addChildEntity(CAstEntity entity) {
        
    }
    
    protected void addInitializer(CAstNode node) {
        
    }
    
    protected void defineMethod(FunctionEntity entity) {
        addInitializer(astBuilder().makeNode(CAstNode.FUNCTION_STMT, astBuilder().makeConstant(entity)));
        addScopedEntity(null, entity);
    }
    
    protected void addScopedEntity(CAstNode scope, FunctionEntity entity) {
    }
    
    @Override
    protected RubyAstVisitor<?> enterMethodDeclaration(MethodDeclaration node) {
        return new MethodState(this);
    }
    
    @Override
    public AbstractState<?> getParentVisitor() {
        return (AbstractState<?>) super.getParentVisitor();
    }
    
    public CAst astBuilder() {
        return context.astBuilder();
    }
    
}