/**
 * 
 */
package com.yoursway.ruby.wala;

import com.ibm.wala.cast.ipa.callgraph.AstCallGraph.ScriptFakeRoot;
import com.ibm.wala.cfg.InducedCFG;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.NewSiteReference;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.callgraph.impl.FakeRootMethod;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ssa.SSAAbstractInvokeInstruction;
import com.ibm.wala.ssa.SSANewInstruction;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

/**
 * Represents a fake "function" that calls every top-level script. Derived to
 * produce custom SSA instructions. (Note: custom instructions are needed because
 * the default ones return Java-specific exception types.)
 * 
 * @author Andrey Tarantsov
 */
public class RubyFakeRoot extends ScriptFakeRoot {
    
    public final static MethodReference fakeRoot = MethodReference.findOrCreate(RubyTypes.FakeRoot,
            FakeRootMethod.name, FakeRootMethod.descr);
    
    public RubyFakeRoot(IClassHierarchy cha, AnalysisOptions options) {
        super(RubyFakeRoot.fakeRoot, cha.lookupClass(RubyTypes.FakeRoot), cha, options);
    }
    
    public InducedCFG makeControlFlowGraph() {
        return new RubyInducedCFG(getStatements(), this, Everywhere.EVERYWHERE);
    }
    
    public SSANewInstruction addAllocation(TypeReference T) {
        if (cha.isSubclassOf(cha.lookupClass(T), cha.lookupClass(RubyTypes.Root))) {
            int instance = nextLocal++;
            NewSiteReference ref = NewSiteReference.make(statements.size(), T);
            RubyNewInstruction result = new RubyNewInstruction(instance, ref);
            statements.add(result);
            return result;
        } else {
            return super.addAllocation(T);
        }
    }
    
    public SSAAbstractInvokeInstruction addDirectCall(int function, int[] params, CallSiteReference site) {
        CallSiteReference newSite = new RubyCallSiteReference(site.getDeclaredTarget(), statements.size());
        
        RubyInvoke s = new RubyInvoke(function, nextLocal++, params, nextLocal++, newSite);
        statements.add(s);
        
        return s;
    }
    
}