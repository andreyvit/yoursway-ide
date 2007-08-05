package com.yoursway.js.wala;

import com.ibm.wala.classLoader.NewSiteReference;
import com.ibm.wala.ipa.summaries.MethodSummary;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

public class JavaScriptSummary extends MethodSummary {

  private final int declaredParameters;

  public JavaScriptSummary(MethodReference ref, int declaredParameters) {
    super(ref);
    this.declaredParameters = declaredParameters;
    addStatement(
      new JavaScriptNewInstruction(
        declaredParameters+1,
	NewSiteReference.make(
          getNextProgramCounter(),
	  JavaScriptTypes.Array)));

  }

  public int getNumberOfParameters() {
    return declaredParameters;
  }

  public TypeReference getParameterType(int i) {
    return JavaScriptTypes.Root;
  }

}

