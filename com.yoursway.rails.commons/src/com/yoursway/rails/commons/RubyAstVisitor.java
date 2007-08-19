package com.yoursway.rails.commons;

import org.eclipse.core.runtime.Assert;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ruby.ast.RubySingletonMethodDeclaration;

/**
 * A Ruby AST visitor, differing from the DLTK's visitor in four ways:
 * <ol>
 * <li>Contains typed enter methods for every intersting node types (please add
 * additional methods when more node types are needed).</li>
 * <li>Enter methods return a visitor that will be used to visit the children
 * and will be called when traversing of the children has finished, or
 * <code>null</code> to skip visiting children.
 * <li>There is a single <code>leave</code> method that is called on the
 * children visitor (i.e. the one returned from the enter method) when
 * traversing leaves the first node it has visited; if the enter method returns
 * <code>null</code> or <code>this</code> (or any other existing visitor),
 * no leave method is called.</li>
 * <li>Enter/leave methods do not throw checked exceptions.</li>
 * </ol>
 * 
 * <p>
 * {@link RubyAstTraverser} should be used to traverse DLTK AST nodes using this
 * visitor.
 * </p>
 * 
 * <p>
 * If you think you need an ability to track leaving from interim nodes, please
 * think again. And if you still aren't convinced, feel free to implement that.
 * </p>
 * 
 * @author Andrey Tarantsov
 */
public class RubyAstVisitor<T extends ASTNode> {
    
    private final RubyAstVisitor<?> parentVisitor;
    
    private final T initialNode;
    
    private ASTNode currentNode;
    
    @SuppressWarnings("unchecked")
    public RubyAstVisitor(RubyAstVisitor<?> parentVisitor) {
        this.parentVisitor = parentVisitor;
        this.initialNode = (parentVisitor == null ? null : (T) parentVisitor.getCurrentNode());
    }
    
    void $verify(RubyAstVisitor<?> parentVisitor, ASTNode initialNode) {
        Assert.isTrue(this.parentVisitor == parentVisitor);
        Assert.isTrue(this.initialNode == initialNode);
    }
    
    protected RubyAstVisitor<?> enterNode(ASTNode node) {
        return this;
    }
    
    protected RubyAstVisitor<?> enterMethodDeclaration(MethodDeclaration node) {
        return enterNode(node);
    }
    
    protected RubyAstVisitor<?> enterSingletonMethodDeclaration(RubySingletonMethodDeclaration node) {
        return enterNode(node);
    }
    
    protected RubyAstVisitor<?> enterCall(CallExpression node) {
        return enterNode(node);
    }
    
    protected RubyAstVisitor<?> enterModuleDeclaration(ModuleDeclaration node) {
        return enterNode(node);
    }
    
    // copy this method to support a new node
    protected RubyAstVisitor<?> enter(MethodDeclaration node) {
        return enterNode(node);
    }
    
    protected RubyAstVisitor<?> enterUnknown(ASTNode node) {
        return enterNode(node);
    }
    
    public final RubyAstVisitor<?> switchEnter(ASTNode node) {
        currentNode = node;
        try {
            if (node instanceof RubySingletonMethodDeclaration)
                return enterSingletonMethodDeclaration((RubySingletonMethodDeclaration) node);
            else if (node instanceof MethodDeclaration)
                return enterMethodDeclaration((MethodDeclaration) node);
            else if (node instanceof CallExpression)
                return enterCall((CallExpression) node);
            else if (node instanceof ModuleDeclaration)
                return enterModuleDeclaration((ModuleDeclaration) node);
            // copy the following two lines to support a new node
            else if (node instanceof MethodDeclaration)
                return enter((MethodDeclaration) node);
            else
                return enterUnknown(node);
        } finally {
            currentNode = null;
        }
    }
    
    public final RubyAstVisitor<?> leaveNode(ASTNode node) {
        // assert: (initialNode != null) => (node != null) 
        Assert.isTrue(initialNode == null || node != null);
        if (node == initialNode) {
            leave();
            return parentVisitor;
        }
        return this;
    }
    
    protected void leave() {
    }
    
    public RubyAstVisitor<?> getParentVisitor() {
        return parentVisitor;
    }
    
    /**
     * Returns the node processing of which has resulted in the creation of this
     * visitor.
     */
    protected T node() {
        return initialNode;
    }
    
    private ASTNode getCurrentNode() {
        if (currentNode == null)
            throw new IllegalStateException("getCurrentNode() can only be called from inside enter*()");
        return currentNode;
    }
    
}
