/**
 * 
 */
package com.yoursway.ruby.wala2.translator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;

import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.ibm.wala.util.collections.HashMapFactory;
import com.yoursway.rails.commons.RubyAstVisitor;

abstract class AbstractModuleOrFuncOrClassState<T extends ASTNode> extends AbstractCodeContainerState<T> {
    
    protected final List<CAstNode> children = new ArrayList<CAstNode>();
    
    public AbstractModuleOrFuncOrClassState(RubyAstVisitor<?> parentVisitor) {
        super(parentVisitor);
    }
    
    @Override
    protected final void addChildNode(CAstNode node) {
        children.add(node);
    }
    
    @Override
    protected RubyAstVisitor<?> enterMethodDeclaration(MethodDeclaration node) {
        return new MethodState(this);
    }
  
    @Override
    protected final void leave() {
        if (!initializers.isEmpty())
            children.add(0, astBuilder().makeNode(CAstNode.BLOCK_STMT, DltkAstToCommonAstTranslator.toArray(initializers)));
        
        final Map<CAstNode, Set<CAstEntity>> subs = HashMapFactory.make();
        for (Map.Entry<CAstNode, HashSet<CAstEntity>> entry : scopedEntities.entrySet())
            subs.put(entry.getKey(), entry.getValue());
        
        final CAstNode ast = astBuilder().makeNode(CAstNode.BLOCK_STMT, DltkAstToCommonAstTranslator.toArray(children));
        createEntity(subs, ast);
    }
    
    protected abstract void createEntity(Map<CAstNode, Set<CAstEntity>> subs, CAstNode ast);
    
}