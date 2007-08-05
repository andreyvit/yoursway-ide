package com.yoursway.js.wala;

import com.ibm.wala.cast.ir.cfg.AstInducedCFG;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.Context;
import com.ibm.wala.ssa.SSAInstruction;

public class JSInducedCFG extends AstInducedCFG {

  public JSInducedCFG(SSAInstruction[] instructions, IMethod method, Context context) {
    super(instructions, method, context);
  }

  class JSPEIVisitor extends AstPEIVisitor implements XInstructionVisitor {

    JSPEIVisitor(boolean[] r) {
      super(r);
    }

    public void visitJavaScriptInvoke(JavaScriptInvoke inst) {
      breakBasicBlock();
    }

    public void visitJavaScriptPropertyRead(JavaScriptPropertyRead inst) {
    }

    public void visitJavaScriptPropertyWrite(JavaScriptPropertyWrite inst) {
    }

    public void visitTypeOf(JavaScriptTypeOfInstruction inst) {
    }
  }
    
  class JSBranchVisitor extends AstBranchVisitor implements XInstructionVisitor {

    JSBranchVisitor(boolean[] r) {
      super(r);
    }

    public void visitJavaScriptInvoke(JavaScriptInvoke inst) {
    }

    public void visitJavaScriptPropertyRead(JavaScriptPropertyRead inst) {
    }

    public void visitJavaScriptPropertyWrite(JavaScriptPropertyWrite inst) {
    }

    public void visitTypeOf(JavaScriptTypeOfInstruction inst) {
    }
  }
    
  protected BranchVisitor makeBranchVisitor(boolean[] r) {
    return new JSBranchVisitor(r);
  }

  protected PEIVisitor makePEIVisitor(boolean[] r) {
    return new JSPEIVisitor(r);
  }

}
