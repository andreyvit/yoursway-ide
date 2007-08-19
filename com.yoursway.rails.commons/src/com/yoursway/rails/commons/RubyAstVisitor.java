package com.yoursway.rails.commons;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ruby.ast.RubySingletonMethodDeclaration;

/**
 * A Ruby AST visitor, differing from the DLTK's visitor in four ways:
 * <ol>
 * <li>Contains typed enter/leave methods for every intersting node types
 * (please add additional methods when more node types are needed).</li>
 * <li>Enter methods return a visitor that will be used to visit the children,
 * or <code>null</code> to skip visiting children.
 * <li>Leave method is not called when enter method returns <code>null</code>.</li>
 * <li>Enter/leave methods do not throw checked exceptions.</li>
 * </ol>
 * 
 * <p>
 * {@link RubyAstTraverser} should be used to traverse DLTK AST nodes using this
 * visitor.
 * </p>
 * 
 * @author Andrey Tarantsov
 */
public class RubyAstVisitor {
    
    private int nestingLevel = 0;
    
    protected RubyAstVisitor enterNode(ASTNode node) {
        return this;
    }
    
    protected void leaveNode(ASTNode node) {
    }
    
    protected RubyAstVisitor enterMethodDeclaration(MethodDeclaration node) {
        return enterNode(node);
    }
    
    protected void leaveMethodDeclaration(MethodDeclaration node) {
        leaveNode(node);
    }
    
    protected RubyAstVisitor enterSingletonMethodDeclaration(RubySingletonMethodDeclaration node) {
        return enterNode(node);
    }
    
    protected void leaveSingletonMethodDeclaration(RubySingletonMethodDeclaration node) {
        leaveNode(node);
    }
    
    protected RubyAstVisitor enterCall(CallExpression node) {
        return enterNode(node);
    }
    
    protected void leaveCall(CallExpression node) {
        leaveNode(node);
    }
    
    protected RubyAstVisitor enterModuleDeclaration(ModuleDeclaration node) {
        return enterNode(node);
    }
    
    protected void leaveModuleDeclaration(ModuleDeclaration node) {
        leaveNode(node);
    }
    
    // copy this and the next method to support a new node
    protected RubyAstVisitor enter(MethodDeclaration node) {
        return enterNode(node);
    }
    
    protected void leave(MethodDeclaration node) {
        leaveNode(node);
    }
    
    protected RubyAstVisitor enterUnknown(ASTNode node) {
        return enterNode(node);
    }
    
    protected void leaveUnknown(ASTNode node) {
        leaveNode(node);
    }
    
    protected void nestedLevelDropped(int newLevel) {
    }
    
    protected final int getNestingLevel() {
        return nestingLevel;
    }
    
    public final RubyAstVisitor switchEnter(ASTNode node) {
        ++nestingLevel;
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
    }
    
    public final void switchLeave(ASTNode node) {
        nestedLevelDropped(--nestingLevel);
        if (node instanceof RubySingletonMethodDeclaration)
            leaveSingletonMethodDeclaration((RubySingletonMethodDeclaration) node);
        else if (node instanceof MethodDeclaration)
            leaveMethodDeclaration((MethodDeclaration) node);
        else if (node instanceof CallExpression)
            leaveCall((CallExpression) node);
        else if (node instanceof ModuleDeclaration)
            leaveModuleDeclaration((ModuleDeclaration) node);
        // copy the following two lines to support a new node
        else if (node instanceof MethodDeclaration)
            leave((MethodDeclaration) node);
        else
            leaveUnknown(node);
    }
    
}
