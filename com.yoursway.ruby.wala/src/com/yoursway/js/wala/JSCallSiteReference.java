package com.yoursway.js.wala;

import com.ibm.wala.cast.types.AstMethodReference;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.shrikeBT.IInvokeInstruction;
import com.ibm.wala.types.MethodReference;

public class JSCallSiteReference extends CallSiteReference {

  // this must be distinct from java invoke codes.
  // see com.ibm.shrikeBT.BytecodeConstants
  public static enum Dispatch implements IInvokeInstruction.IDispatch {
    JS_CALL;
  }

  public JSCallSiteReference(MethodReference ref, int pc) {
    super(pc, ref);
  }

  public JSCallSiteReference(int pc) {
    this(AstMethodReference.fnReference(JavaScriptTypes.CodeBody), pc);
  }

  public IInvokeInstruction.IDispatch getInvocationCode() {
    return Dispatch.JS_CALL;
  }

  public String toString() {
    return "JSCall@" + getProgramCounter();
  }

}
