package com.yoursway.ruby.wala.translator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ast.expressions.CallExpression;

import com.ibm.wala.cast.tree.CAstNode;
import com.yoursway.rails.commons.RubyAstVisitor;

public class CallState extends AbstractState<CallExpression> {

    public CallState(RubyAstVisitor<?> parentVisitor) {
        super(parentVisitor);
    }
    
    @Override
    protected void leave() {
        List<CAstNode> children = new ArrayList<CAstNode>();
        children.add(astBuilder().makeNode(CAstNode.VAR, astBuilder().makeConstant("boz")));
        children.add(astBuilder().makeConstant("do"));
        
        CAstNode call = astBuilder().makeNode(CAstNode.CALL, children.toArray(new CAstNode[children.size()]));
        getParentVisitor().addChildNode(call);
        mapNodeToItself(call);
    }
    
}
