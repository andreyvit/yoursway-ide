package com.yoursway.ruby.wala;

import com.ibm.wala.cast.ipa.callgraph.AstCFAPointerKeys;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.cha.IClassHierarchy;

/**
 * Common utilities for CFA-style call graph builders.
 */
public class JSCFABuilder extends JSSSAPropagationCallGraphBuilder {

  public JSCFABuilder(IClassHierarchy cha, AnalysisOptions options) {
    super(cha, options, new AstCFAPointerKeys());
  }

}
