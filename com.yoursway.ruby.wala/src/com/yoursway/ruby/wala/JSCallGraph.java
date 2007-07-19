package com.yoursway.ruby.wala;

import com.ibm.wala.cast.ipa.callgraph.AstCallGraph;
import com.ibm.wala.cfg.InducedCFG;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.NewSiteReference;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.callgraph.impl.FakeRootMethod;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ssa.SSAAbstractInvokeInstruction;
import com.ibm.wala.ssa.SSANewInstruction;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

public class JSCallGraph extends AstCallGraph {

  public JSCallGraph(IClassHierarchy cha, AnalysisOptions options) {
    super(cha, options);
  }

  public final static MethodReference fakeRoot = MethodReference.findOrCreate(JavaScriptTypes.FakeRoot, FakeRootMethod.name, FakeRootMethod.descr);

  public static class JSFakeRoot extends ScriptFakeRoot {

    public JSFakeRoot(IClassHierarchy cha, 
		      AnalysisOptions options)
    {
      super(fakeRoot, cha.lookupClass(JavaScriptTypes.FakeRoot), cha, options);
    }

    public InducedCFG makeControlFlowGraph() {
      return new JSInducedCFG(getStatements(), this, Everywhere.EVERYWHERE);
    }

    public SSANewInstruction addAllocation(TypeReference T) {
      if (cha.isSubclassOf(cha.lookupClass(T), cha.lookupClass(JavaScriptTypes.Root))) {
        int instance = nextLocal++;
        NewSiteReference ref = NewSiteReference.make(statements.size(), T);
        SSANewInstruction result = new JavaScriptNewInstruction(instance, ref);
        statements.add(result);
        return result;
      } else {
        return super.addAllocation(T);
      }
    }

    public SSAAbstractInvokeInstruction addDirectCall(int function, int[] params, CallSiteReference site) {
      CallSiteReference newSite = CallSiteReference.make(statements.size(), site.getDeclaredTarget(), site.getInvocationCode());

      JavaScriptInvoke s = new JavaScriptInvoke(function, nextLocal++, params, nextLocal++, newSite);
      statements.add(s);

      return s;
    }
  }

  protected CGNode makeFakeRootNode() {
      return findOrCreateNode(new JSFakeRoot(cha, options), Everywhere.EVERYWHERE);
  }
}
