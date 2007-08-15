package com.yoursway.ruby.wala;

import com.ibm.wala.cast.ir.cfg.AstInducedCFG;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.Context;
import com.ibm.wala.ssa.SSAInstruction;

public class RubyInducedCFG extends AstInducedCFG {
    
    public RubyInducedCFG(SSAInstruction[] instructions, IMethod method, Context context) {
        super(instructions, method, context);
    }
    
    class RubyPEIVisitor extends AstPEIVisitor implements RubyCustomIntructionsVisitor {
        
        RubyPEIVisitor(boolean[] r) {
            super(r);
        }
        
        public void visitRubyInvoke(RubyInvoke instruction) {
            breakBasicBlock();
        }
        
    }
    
    class RubyBranchVisitor extends AstBranchVisitor implements RubyCustomIntructionsVisitor {
        
        RubyBranchVisitor(boolean[] r) {
            super(r);
        }
        
        public void visitRubyInvoke(RubyInvoke instruction) {
        }
        
    }
    
    protected BranchVisitor makeBranchVisitor(boolean[] r) {
        return new RubyBranchVisitor(r);
    }
    
    protected PEIVisitor makePEIVisitor(boolean[] r) {
        return new RubyPEIVisitor(r);
    }
    
}
