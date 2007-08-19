package com.yoursway.ruby.wala;

import org.eclipse.core.runtime.Assert;

import com.ibm.wala.cast.types.AstMethodReference;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.shrikeBT.IInvokeInstruction;
import com.ibm.wala.shrikeBT.IInvokeInstruction.IDispatch;
import com.ibm.wala.types.MethodReference;

public class RubyCallSiteReference extends CallSiteReference {
    
    // this must be distinct from java invoke codes.
    // see com.ibm.shrikeBT.BytecodeConstants
    public static enum Dispatch implements IInvokeInstruction.IDispatch {
        RUBY_CALL;
    }
    
    public RubyCallSiteReference(MethodReference ref, int pc) {
        super(pc, ref);
    }
    
    public RubyCallSiteReference(int pc) {
        this(AstMethodReference.fnReference(RubyTypes.CodeBody), pc);
    }
    
    public IInvokeInstruction.IDispatch getInvocationCode() {
        return Dispatch.RUBY_CALL;
    }
    
    @Override
    protected String getInvocationString(IDispatch invocationCode) {
        Assert.isTrue(invocationCode == Dispatch.RUBY_CALL);
        return "rubycall";
    }
    
    public String toString() {
        return "RubyCall@" + getProgramCounter();
    }
    
}
