package com.yoursway.ruby.wala;

import com.ibm.wala.cfg.InducedCFG;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.summaries.MethodSummary;
import com.ibm.wala.ipa.summaries.SummarizedMethod;
import com.ibm.wala.types.MethodReference;

public class RubySummarizedFunction extends SummarizedMethod {

  public RubySummarizedFunction(MethodReference ref, 
				      MethodSummary summary, 
				      IClass declaringClass) 
  {
    super(ref, summary, declaringClass);
  }

  public boolean equals(Object o) {
    return this==o;
  }

  public InducedCFG makeControlFlowGraph() {
    return new RubyInducedCFG(getStatements(), this, Everywhere.EVERYWHERE);
  }

}
