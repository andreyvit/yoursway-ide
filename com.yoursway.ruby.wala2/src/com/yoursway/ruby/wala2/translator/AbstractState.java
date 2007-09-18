/**
 * 
 */
package com.yoursway.ruby.wala2.translator;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.expressions.CallExpression;

import com.ibm.wala.cast.tree.CAst;
import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.yoursway.rails.commons.RubyAstVisitor;
import com.yoursway.ruby.wala2.entities.MethodEntity;

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
        AbstractState<?> parent = getParentVisitor();
        if (parent != null)
            parent.addChildNode(node);
    }
    
    protected void addChildEntity(CAstEntity entity) {
        
    }
    
    protected void addInitializer(CAstNode node) {
        
    }
    
    protected void mapNodeToItself(CAstNode node) {
        getParentVisitor().mapNodeToItself(node);
    }
    
//    protected void addCallFlowEdge(CAstNode node) {
//        getParentVisitor().mapNodeToItself(node);
//    }
    
    protected void defineMethod(MethodEntity entity) {
//        addInitializer(astBuilder().makeNode(CAstNode.FUNCTION_STMT, astBuilder().makeConstant(entity)));
        addScopedEntity(null, entity);
    }
    
    protected void addScopedEntity(CAstNode scope, CAstEntity entity) {
    }
    
    @Override
    public AbstractState<?> getParentVisitor() {
        return (AbstractState<?>) super.getParentVisitor();
    }
    
    public CAst astBuilder() {
        return context.astBuilder();
    }
    
    @Override
    protected RubyAstVisitor<?> enterCall(CallExpression node) {
        return new CallState(this);
    }
 
}