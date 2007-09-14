package com.yoursway.ruby.wala2.client;

import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.ContextSelector;
import com.ibm.wala.ipa.callgraph.ReflectionSpecification;
import com.ibm.wala.ipa.callgraph.impl.ExplicitCallGraph;
import com.ibm.wala.ipa.callgraph.propagation.SSAContextInterpreter;
import com.ibm.wala.ipa.callgraph.propagation.cfa.ZeroXCFABuilder;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.yoursway.ruby.wala2.ipa.callgraph.RubyCallGraph;

public class RubyZeroXCFABuilder extends ZeroXCFABuilder {

	public RubyZeroXCFABuilder(IClassHierarchy cha, AnalysisOptions options,
			AnalysisCache cache, ContextSelector appContextSelector,
			SSAContextInterpreter appContextInterpreter,
			ReflectionSpecification reflect, int instancePolicy) {
		super(cha, options, cache, appContextSelector, appContextInterpreter,
				reflect, instancePolicy);
	}

	@Override
	protected ExplicitCallGraph createEmptyCallGraph(IClassHierarchy cha,
			AnalysisOptions options) {
		return new RubyCallGraph(cha, options, getAnalysisCache());
	}

}
