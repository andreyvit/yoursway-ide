package com.yoursway.ruby.wala;

import com.ibm.wala.cast.ipa.callgraph.StandardFunctionTargetSelector;
import com.ibm.wala.client.CallGraphBuilderFactory;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CallGraphBuilder;
import com.ibm.wala.ipa.callgraph.propagation.cfa.ZeroXInstanceKeys;
import com.ibm.wala.ipa.cha.IClassHierarchy;

/**
 * @author Julian Dolby (dolby@us.ibm.com)
 * 
 * A factory to create call graph builders using 0-CFA
 */
public class ZeroCFABuilderFactory implements CallGraphBuilderFactory {

  public CallGraphBuilder make(AnalysisOptions options,
			       IClassHierarchy cha,
			       AnalysisScope scope,
			       boolean keepPointsTo)
  {
    com.ibm.wala.ipa.callgraph.impl.Util.addDefaultSelectors(options, cha);
    options.setSelector(new StandardFunctionTargetSelector(cha, options.getMethodTargetSelector()));

    return new JSZeroXCFABuilder(cha, options, null, null, null, ZeroXInstanceKeys.NONE);
  }
}
