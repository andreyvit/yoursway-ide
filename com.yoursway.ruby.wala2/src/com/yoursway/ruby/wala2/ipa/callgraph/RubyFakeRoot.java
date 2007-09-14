package com.yoursway.ruby.wala2.ipa.callgraph;

import com.ibm.wala.cast.ipa.callgraph.AstCallGraph.ScriptFakeRoot;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.impl.FakeRootMethod;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ssa.SSAAbstractInvokeInstruction;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;
import com.yoursway.ruby.wala2.RubyLanguage;

public class RubyFakeRoot extends ScriptFakeRoot {

	public static final TypeReference FakeRoot = TypeReference.findOrCreate(
			RubyLanguage.LOADER_REF, "Object");

	public final static MethodReference fakeRoot = MethodReference
			.findOrCreate(FakeRoot, FakeRootMethod.name, FakeRootMethod.descr);

	public RubyFakeRoot(IClassHierarchy cha, AnalysisOptions options,
			AnalysisCache cache) {
		super(RubyFakeRoot.fakeRoot, cha.lookupClass(FakeRoot), cha, options,
				cache);
	}

	@Override
	public SSAAbstractInvokeInstruction addDirectCall(int functionVn,
			int[] argVns, CallSiteReference callSite) {
		throw new UnsupportedOperationException();
	}

}
