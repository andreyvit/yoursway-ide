package com.yoursway.ruby.wala2.translator;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.expressions.CallExpression;

import com.yoursway.rails.commons.RubyAstTraverser;
import com.yoursway.rails.commons.RubyAstVisitor;

public class CallState extends AbstractState<CallExpression> {

    public CallState(RubyAstVisitor<?> parentVisitor) {
        super(parentVisitor);
    }
    
    @Override
    protected void leave() {
    	ASTNode receiver = node().getReceiver();
    	RubyAstTraverser traverser = new RubyAstTraverser();
    	traverser.traverse(receiver, new SingleNodeCollectingState(this));
//        List<CAstNode> children = new ArrayList<CAstNode>();
//        children.add(astBuilder().makeNode(CAstNode.VAR, astBuilder().makeConstant(node().getName())));
//        children.add(astBuilder().makeConstant("do"));
//        
//        CAstNode call = astBuilder().makeNode(CAstNode.CALL, children.toArray(new CAstNode[children.size()]));
//        getParentVisitor().addChildNode(call);
//        mapNodeToItself(call);
    	System.out.println("CallState.leave()");
//    	throw new UnsupportedOperationException();
    }
    
}
