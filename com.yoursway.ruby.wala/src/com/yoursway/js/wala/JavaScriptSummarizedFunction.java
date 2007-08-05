package com.yoursway.js.wala;

import com.ibm.wala.cfg.InducedCFG;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.summaries.MethodSummary;
import com.ibm.wala.ipa.summaries.SummarizedMethod;
import com.ibm.wala.types.MethodReference;

public class JavaScriptSummarizedFunction extends SummarizedMethod {

  public JavaScriptSummarizedFunction(MethodReference ref, 
				      MethodSummary summary, 
				      IClass declaringClass) 
  {
    super(ref, summary, declaringClass);
  }

  public boolean equals(Object o) {
    return this==o;
  }

  public InducedCFG makeControlFlowGraph() {
    return new JSInducedCFG(getStatements(), this, Everywhere.EVERYWHERE);
  }

}
