/**
 * 
 */
package com.yoursway.ruby.wala.translator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.ast.ASTNode;

import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.ibm.wala.cast.tree.impl.CAstControlFlowRecorder;
import com.ibm.wala.cast.tree.impl.CAstSourcePositionRecorder;
import com.ibm.wala.util.collections.HashMapFactory;
import com.yoursway.rails.commons.RubyAstVisitor;
import com.yoursway.ruby.wala.entities.FunctionEntity;

abstract class AbstractCodeContainerState<T extends ASTNode> extends AbstractState<T> {
    
    public AbstractCodeContainerState(RubyAstVisitor<?> parentVisitor) {
        super(parentVisitor);
    }
    
    protected final Map<CAstNode, HashSet<CAstEntity>> scopedEntities = HashMapFactory.make();
    
    protected final List<CAstNode> initializers = new ArrayList<CAstNode>();
    
    private final CAstSourcePositionRecorder pos = new CAstSourcePositionRecorder();
    
    private final CAstControlFlowRecorder cfg = new CAstControlFlowRecorder(pos);
    
    CAstControlFlowRecorder cfg() {
        return cfg;
    }
    
    CAstSourcePositionRecorder pos() {
        return pos;
    }
    
    @Override
    protected void addInitializer(CAstNode node) {
        initializers.add(node);
    }
    
    @Override
    protected void addScopedEntity(CAstNode scope, FunctionEntity entity) {
        HashSet<CAstEntity> set = scopedEntities.get(scope);
        if (set == null) {
            set = new HashSet<CAstEntity>();
            scopedEntities.put(scope, set);
        }
        set.add(entity);
    }

}