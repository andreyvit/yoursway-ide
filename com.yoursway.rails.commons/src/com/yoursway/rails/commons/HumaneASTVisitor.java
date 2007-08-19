package com.yoursway.rails.commons;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ruby.ast.RubySingletonMethodDeclaration;

/**
 * A Ruby AST visitor, differing from the DLTK's visitor in four ways:
 * <ol>
 * <li>Contains typed enter/leave methods for every intersting node types
 * (please add additional methods when more node types are needed).</li>
 * <li>Leave method is not called when enter method returns <code>false</code>.</li>
 * <li>Node nesting level is tracked, so that it's easy to tell when certain
 * constructs are closed by monitoring the nesting level.</li>
 * <li>Enter/leave methods do not throw checked exceptions.</li>
 * </ol>
 * 
 * @author Andrey Tarantsov
 */
public class HumaneASTVisitor {
    
    private int nestingLevel = 0;
    
    private final ASTVisitor adapter = new ASTVisitor() {
        
        private int ignoreEnds = 0;
        
        @Override
        public void endvisitGeneral(ASTNode node) {
            if (ignoreEnds > 0)
                --ignoreEnds;
            else
                switchLeave(node);
        }
        
        @Override
        public boolean visitGeneral(ASTNode node) {
            boolean result = switchEnter(node);
            if (!result)
                ignoreEnds++;
            return result;
        }
        
    };
    
    protected boolean enterNode(ASTNode node) {
        return true;
    }
    
    protected void leaveNode(ASTNode node) {
    }
    
    protected boolean enterMethodDeclaration(MethodDeclaration node) {
        return enterNode(node);
    }
    
    protected void leaveMethodDeclaration(MethodDeclaration node) {
        leaveNode(node);
    }
    
    protected boolean enterSingletonMethodDeclaration(RubySingletonMethodDeclaration node) {
        return enterNode(node);
    }
    
    protected void leaveSingletonMethodDeclaration(RubySingletonMethodDeclaration node) {
        leaveNode(node);
    }
    
    protected boolean enterCall(CallExpression node) {
        return enterNode(node);
    }
    
    protected void leaveCall(CallExpression node) {
        leaveNode(node);
    }
    
    protected boolean enterModuleDeclaration(ModuleDeclaration node) {
        return enterNode(node);
    }
    
    protected void leaveModuleDeclaration(ModuleDeclaration node) {
        leaveNode(node);
    }
    
    // copy this and the next method to support a new node
    protected boolean enter(MethodDeclaration node) {
        return enterNode(node);
    }
    
    protected void leave(MethodDeclaration node) {
        leaveNode(node);
    }
    
    protected boolean enterUnknown(ASTNode node) {
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
    
    public final boolean switchEnter(ASTNode node) {
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
    
    public final ASTVisitor getAdapter() {
        return adapter;
    }
    
    public void traverse(ASTNode node) {
        try {
            node.traverse(adapter);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
