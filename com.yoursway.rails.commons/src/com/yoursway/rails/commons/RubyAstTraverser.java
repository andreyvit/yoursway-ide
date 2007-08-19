package com.yoursway.rails.commons;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;

/**
 * Traverses a DLTK AST using {@link RubyAstVisitor}s.
 * 
 * While this object does hold some internal data, it has no user-visible state.
 * 
 * @author Andrey Tarantsov
 */
public final class RubyAstTraverser {
    
    private final ASTVisitor adapter = new ASTVisitor() {
        
        private int ignoreEnds = 0;
        
        @Override
        public void endvisitGeneral(ASTNode node) {
            if (ignoreEnds > 0)
                --ignoreEnds;
            else
                leave(node);
        }
        
        @Override
        public boolean visitGeneral(ASTNode node) {
            boolean result = enter(node);
            if (!result)
                ignoreEnds++;
            return result;
        }
        
    };
    
    /**
     * Traverses the given node with the given visitor. If the visitor's enter
     * method returns some visitor, uses that visitor to recursively traverse
     * the children of the given node (and so forth).
     * 
     * This method is reenterable, so it can be called from inside a visitor on
     * the same traverser object.
     * 
     * This method provides a strong exception safety guarantee: if an exception
     * interrupts the traversing in progress, the object will be left in exactly
     * the same state as it was before calling this method. (In fact, upon
     * successful completion of this method the object is also left in an
     * unchanged state.)
     * 
     * This method must be called with a newly created visitor that does not
     * have a parent (i.e. <code>null</code> was passed into the constructor).
     * 
     * @param node
     *            a node to traverse.
     * @param rootVisitor
     *            a visitor that will visit the given node.
     */
    public void traverse(ASTNode node, RubyAstVisitor rootVisitor) {
        Assert.isNotNull(node);
        Assert.isNotNull(rootVisitor);
        Assert.isTrue(rootVisitor.getParentVisitor() == null);
        try {
            RubyAstVisitor outerVisitor = currentVisitor;
            currentVisitor = rootVisitor;
            try {
                node.traverse(adapter);
                rootVisitor.leaveNode(null);
            } finally {
                currentVisitor = outerVisitor;
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private RubyAstVisitor currentVisitor;
    
    private boolean enter(ASTNode node) {
        RubyAstVisitor childrenVisitor = currentVisitor.switchEnter(node);
        if (childrenVisitor == null)
            return false;
        childrenVisitor.$verify(currentVisitor, node);
        currentVisitor = childrenVisitor;
        return true;
    }
    
    private void leave(ASTNode node) {
        currentVisitor = currentVisitor.leaveNode(node);
    }
    
}
