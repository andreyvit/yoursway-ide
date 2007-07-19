package com.yoursway.ruby.wala;

import java.util.Collection;

import com.ibm.wala.ssa.SSAAbstractUnaryInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SymbolTable;
import com.ibm.wala.ssa.ValueDecorator;
import com.ibm.wala.types.TypeReference;

public class JavaScriptTypeOfInstruction extends SSAAbstractUnaryInstruction {

  public JavaScriptTypeOfInstruction(int lval, int object) {
    super(lval, object);
  }

  public SSAInstruction copyForSSA(int[] defs, int[] uses) {
    return new JavaScriptTypeOfInstruction((defs != null ? defs[0] : getDef(0)), (uses != null ? uses[0] : getUse(0)));
  }

  public String toString(SymbolTable symbolTable, ValueDecorator d) {
    return getValueString(symbolTable, d, getDef(0)) + " = typeof(" + getValueString(symbolTable, d, getUse(0)) + ")";
  }

  public void visit(IVisitor v) {
    ((XInstructionVisitor) v).visitTypeOf(this);
  }

  public Collection<TypeReference> getExceptionTypes() {
    return Util.noExceptions();
  }

}
