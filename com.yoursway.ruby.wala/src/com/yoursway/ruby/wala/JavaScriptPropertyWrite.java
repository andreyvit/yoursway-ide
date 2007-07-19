package com.yoursway.ruby.wala;

import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.debug.Assertions;

import com.ibm.wala.cast.ir.ssa.*;
import com.ibm.wala.ssa.*;

import java.util.*;

public class JavaScriptPropertyWrite extends AbstractReflectivePut {
  
  public JavaScriptPropertyWrite(int objectRef, int memberRef, int value) {
    super(objectRef, memberRef, value);
  }

  public SSAInstruction copyForSSA(int[] defs, int[] uses) {
    return
      new JavaScriptPropertyWrite(
        uses==null? getObjectRef(): uses[0],
        uses==null? getMemberRef(): uses[1],
	uses==null? getValue(): uses[2]);
  }

  public String toString(SymbolTable symbolTable, ValueDecorator d) {
    return super.toString(symbolTable, d) +
	" = " + 
	getValueString(symbolTable, d, getValue());
  }

  /**
   * @see com.ibm.domo.ssa.Instruction#visit(Visitor)
   */
  public void visit(IVisitor v) {
    Assertions._assert(v instanceof XInstructionVisitor);
    ((XInstructionVisitor)v).visitJavaScriptPropertyWrite(this);
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

}