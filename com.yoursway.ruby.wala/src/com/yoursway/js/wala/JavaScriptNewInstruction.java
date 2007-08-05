package com.yoursway.js.wala;

import java.util.Collection;

import com.ibm.wala.classLoader.NewSiteReference;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSANewInstruction;
import com.ibm.wala.types.TypeReference;

public class JavaScriptNewInstruction extends SSANewInstruction {
  
  public JavaScriptNewInstruction(int result, NewSiteReference site) {
    super(result, site);
  }

  public SSAInstruction copyForSSA(int[] defs, int[] uses) {
    return
      new JavaScriptNewInstruction(
        defs==null? getDef(): defs[0],
	getNewSite());
  }

  public Collection<TypeReference> getExceptionTypes() {
    return Util.typeErrorExceptions();
  }

}
