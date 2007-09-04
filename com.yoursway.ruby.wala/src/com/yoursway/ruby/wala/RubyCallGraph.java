package com.yoursway.ruby.wala;

import com.ibm.wala.cast.ipa.callgraph.AstCallGraph;
import com.ibm.wala.cast.ipa.callgraph.AstCallGraph.ScriptFakeRoot;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.callgraph.impl.FakeWorldClinitMethod;
import com.ibm.wala.ipa.cha.IClassHierarchy;

/**
 * Overrides {@link AstCallGraph} to creates a Ruby-specific fake root. This is
 * needed for several reasons.
 * 
 * <p>
 * One reason is that the fake root must at least be derived from
 * {@link ScriptFakeRoot}, or else doing analysis results in a
 * {@link ClassCastException} (I don't exactly remember where).
 * </p>
 * 
 * <p>
 * For other reasons see {@link RubyFakeRoot}.
 * </p>
 * 
 * @author Andrey Tarantsov
 */
public class RubyCallGraph extends AstCallGraph {
    

	public RubyCallGraph(IClassHierarchy cha, AnalysisOptions options, AnalysisCache cache) {
        super(cha, options, cache);
    }
    
    protected CGNode makeFakeRootNode() {
        return findOrCreateNode(new RubyFakeRoot(cha, options, getAnalysisCache()), Everywhere.EVERYWHERE);
    }
    
    @Override
    protected CGNode makeFakeWorldClinitNode() {
      return findOrCreateNode(new FakeWorldClinitMethod(cha, options, getAnalysisCache()), Everywhere.EVERYWHERE);
    }

}
