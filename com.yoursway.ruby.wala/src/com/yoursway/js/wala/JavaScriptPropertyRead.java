package com.yoursway.js.wala;

import java.util.Collection;

import com.ibm.wala.cast.ir.ssa.AbstractReflectiveGet;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.debug.Assertions;

public class JavaScriptPropertyRead extends AbstractReflectiveGet {
  public JavaScriptPropertyRead(int result, int objectRef, int memberRef) {
    super(result, objectRef, memberRef);
  }

  public SSAInstruction copyForSSA(int[] defs, int[] uses) {
    return
      new JavaScriptPropertyRead(
        defs==null? getDef(): defs[0],
	uses==null? getObjectRef(): uses[0],
	uses==null? getMemberRef(): uses[1]);
  }

  /* (non-Javadoc)
   * @see com.ibm.domo.ssa.Instruction#isPEI()
   */
  public boolean isPEI() {
    return true;
  }

  /* (non-Javadoc)
   * @see com.ibm.domo.ssa.Instruction#getExceptionTypes()
   */
  public Collection<TypeReference> getExceptionTypes() {
    return Util.typeErrorExceptions();
  }

  /**
  /* (non-Javadoc)
   * @see com.ibm.domo.ssa.SSAInstruction#visit(com.ibm.domo.ssa.SSAInstruction.Visitor)
   */
  public void visit(IVisitor v) {
    Assertions._assert(v instanceof XInstructionVisitor);
    ((XInstructionVisitor)v).visitJavaScriptPropertyRead(this);
  }
}
