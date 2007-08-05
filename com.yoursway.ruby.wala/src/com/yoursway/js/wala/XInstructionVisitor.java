package com.yoursway.js.wala;

import com.ibm.wala.cast.ir.ssa.AstInstructionVisitor;

public interface XInstructionVisitor extends AstInstructionVisitor {

  public void visitJavaScriptInvoke(JavaScriptInvoke instruction);
    
  public void visitTypeOf(JavaScriptTypeOfInstruction instruction);
    
  public void visitJavaScriptPropertyRead(JavaScriptPropertyRead instruction);
  
  public void visitJavaScriptPropertyWrite(JavaScriptPropertyWrite instruction);
}

