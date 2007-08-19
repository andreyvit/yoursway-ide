package com.yoursway.rails.commons;

import java.util.ArrayList;

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
     * @param node
     *            a node to traverse.
     * @param rootVisitor
     *            a visitor that will visit the given node.
     */
    public void traverse(ASTNode node, RubyAstVisitor rootVisitor) {
        try {
            int waterLevel = visitors.size();
            visitors.add(rootVisitor);
            try {
                node.traverse(adapter);
            } finally {
                while (visitors.size() > waterLevel)
                    visitors.remove(visitors.size() - 1);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private final ArrayList<RubyAstVisitor> visitors = new ArrayList<RubyAstVisitor>();
    
    private boolean enter(ASTNode node) {
        RubyAstVisitor currentVisitor = visitors.get(visitors.size() - 1);
        RubyAstVisitor childrenVisitor = currentVisitor.switchEnter(node);
        if (childrenVisitor == null)
            return false;
        visitors.add(childrenVisitor);
        return true;
    }
    
    private void leave(ASTNode node) {
        RubyAstVisitor currentVisitor = visitors.remove(visitors.size() - 1);
        currentVisitor.switchLeave(node);
    }
    
}
