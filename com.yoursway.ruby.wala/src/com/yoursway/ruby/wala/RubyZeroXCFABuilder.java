package com.yoursway.ruby.wala;

import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.ContextSelector;
import com.ibm.wala.ipa.callgraph.ReflectionSpecification;
import com.ibm.wala.ipa.callgraph.impl.DefaultContextSelector;
import com.ibm.wala.ipa.callgraph.impl.DelegatingContextSelector;
import com.ibm.wala.ipa.callgraph.propagation.SSAContextInterpreter;
import com.ibm.wala.ipa.callgraph.propagation.cfa.ZeroXInstanceKeys;
import com.ibm.wala.ipa.cha.IClassHierarchy;

/**
 * @author sfink
 * 
 * 0-1-CFA Call graph builder, optimized to not disambiguate instances of
 * "uninteresting" types
 */
public class RubyZeroXCFABuilder extends RubyCFABuilder {

  public RubyZeroXCFABuilder(IClassHierarchy cha, AnalysisOptions options, ContextSelector appContextSelector,
      SSAContextInterpreter appContextInterpreter, ReflectionSpecification reflect, int instancePolicy) {
    super(cha, options);

    SSAContextInterpreter contextInterpreter = makeDefaultContextInterpreters(appContextInterpreter, options, cha, reflect);
    setContextInterpreter(contextInterpreter);

//    options.setSelector(new JavaScriptConstructTargetSelector(cha, options.getMethodTargetSelector()));
    com.ibm.wala.ipa.callgraph.impl.Util.addDefaultSelectors(options, cha);
//    options.setSelector(new JavaScriptConstructTargetSelector(cha, options.getMethodTargetSelector()));

    ContextSelector def = new DefaultContextSelector(cha, options.getMethodTargetSelector());
    ContextSelector contextSelector = appContextSelector == null ? def : new DelegatingContextSelector(appContextSelector, def);

    setContextSelector(contextSelector);

    setInstanceKeys(new RubyScopeMappingInstanceKeys(cha, this, new ZeroXInstanceKeys(options, cha, contextInterpreter,
        instancePolicy)));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.domo.ipa.callgraph.propagation.PropagationCallGraphBuilder#getDefaultDispatchBoundHeuristic()
   */
  protected byte getDefaultDispatchBoundHeuristic() {
    return AnalysisOptions.NO_DISPATCH_BOUND;
  }
}
