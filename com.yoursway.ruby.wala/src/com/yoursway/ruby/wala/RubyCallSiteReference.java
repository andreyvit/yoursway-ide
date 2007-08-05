package com.yoursway.ruby.wala;

import com.ibm.wala.cast.types.AstMethodReference;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.shrikeBT.IInvokeInstruction;
import com.ibm.wala.types.MethodReference;

public class RubyCallSiteReference extends CallSiteReference {

  // this must be distinct from java invoke codes.
  // see com.ibm.shrikeBT.BytecodeConstants
  public static enum Dispatch implements IInvokeInstruction.IDispatch {
    JS_CALL;
  }

  public RubyCallSiteReference(MethodReference ref, int pc) {
    super(pc, ref);
  }

  public RubyCallSiteReference(int pc) {
    this(AstMethodReference.fnReference(RubyTypes.CodeBody), pc);
  }

  public IInvokeInstruction.IDispatch getInvocationCode() {
    return Dispatch.JS_CALL;
  }

  public String toString() {
    return "JSCall@" + getProgramCounter();
  }

}
