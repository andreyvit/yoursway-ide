package com.yoursway.js.wala;

import java.util.Set;

import com.ibm.wala.analysis.typeInference.TypeInference;
import com.ibm.wala.cast.ipa.callgraph.AstSSAPropagationCallGraphBuilder;
import com.ibm.wala.cast.ir.ssa.AstIsDefinedInstruction;
import com.ibm.wala.cast.ir.ssa.EachElementHasNextInstruction;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.fixedpoint.impl.AbstractOperator;
import com.ibm.wala.fixedpoint.impl.UnaryOperator;
import com.ibm.wala.fixpoint.IVariable;
import com.ibm.wala.fixpoint.IntSetVariable;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.impl.ExplicitCallGraph;
import com.ibm.wala.ipa.callgraph.propagation.ConcreteTypeKey;
import com.ibm.wala.ipa.callgraph.propagation.ConstantKey;
import com.ibm.wala.ipa.callgraph.propagation.FilteredPointerKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKeyFactory;
import com.ibm.wala.ipa.callgraph.propagation.LocalPointerKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerAnalysis;
import com.ibm.wala.ipa.callgraph.propagation.PointerFlowGraph;
import com.ibm.wala.ipa.callgraph.propagation.PointerFlowGraphFactory;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerKeyFactory;
import com.ibm.wala.ipa.callgraph.propagation.PointsToMap;
import com.ibm.wala.ipa.callgraph.propagation.PointsToSetVariable;
import com.ibm.wala.ipa.callgraph.propagation.PropagationCallGraphBuilder;
import com.ibm.wala.ipa.callgraph.propagation.PropagationSystem;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.shrikeBT.BinaryOpInstruction;
import com.ibm.wala.shrikeBT.UnaryOpInstruction;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSABinaryOpInstruction;
import com.ibm.wala.ssa.SSAUnaryOpInstruction;
import com.ibm.wala.ssa.SymbolTable;
import com.ibm.wala.ssa.SSACFG.BasicBlock;
import com.ibm.wala.util.collections.HashSetFactory;
import com.ibm.wala.util.debug.Trace;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.util.intset.IntSetAction;
import com.ibm.wala.util.intset.IntSetUtil;
import com.ibm.wala.util.intset.MutableIntSet;
import com.ibm.wala.util.intset.MutableMapping;

public class JSSSAPropagationCallGraphBuilder extends AstSSAPropagationCallGraphBuilder {

  public static final boolean DEBUG_LEXICAL = false;

  public static final boolean DEBUG_TYPE_INFERENCE = false;

  protected JSSSAPropagationCallGraphBuilder(IClassHierarchy cha, AnalysisOptions options, PointerKeyFactory pointerKeyFactory) {
    super(cha, options, pointerKeyFactory);
  }

  protected boolean isConstantRef(SymbolTable symbolTable, int valueNumber) {
    if (valueNumber == -1) {
      return false;
    }
    return symbolTable.isConstant(valueNumber);
  }

  // ///////////////////////////////////////////////////////////////////////////
  //
  // language specialization interface
  //
  // ///////////////////////////////////////////////////////////////////////////

  protected boolean useObjectCatalog() {
    return true;
  }

  // ///////////////////////////////////////////////////////////////////////////
  //
  // top-level node constraint generation
  //
  // ///////////////////////////////////////////////////////////////////////////

  protected ExplicitCallGraph createEmptyCallGraph(IClassHierarchy cha, AnalysisOptions options) {
    return new JSCallGraph(cha, options);
  }

  protected TypeInference makeTypeInference(IR ir, IClassHierarchy cha) {
    TypeInference ti = new JSTypeInference(ir, cha);
    ti.solve();

    if (DEBUG_TYPE_INFERENCE) {
      Trace.println("IR of " + ir.getMethod());
      Trace.println(ir);
      Trace.println("TypeInference of " + ir.getMethod());
      for (int i = 0; i < ir.getSymbolTable().getMaxValueNumber(); i++) {
        if (ti.isUndefined(i)) {
          Trace.println("  value " + i + " is undefined");
        } else {
          Trace.println("  value " + i + " has type " + ti.getType(i));
        }
      }
    }

    return ti;
  }

  protected void addAssignmentsForCatchPointerKey(PointerKey exceptionVar, Set catchClasses, PointerKey e) {
    system.newConstraint(exceptionVar, assignOperator, e);
  }

  public static class RubyInterestingVisitor extends AstInterestingVisitor {
    public RubyInterestingVisitor(int vn) {
      super(vn);
    }
  }

  protected InterestingVisitor makeInterestingVisitor(CGNode node, int vn) {
    return new RubyInterestingVisitor(vn);
  }

  // ///////////////////////////////////////////////////////////////////////////
  //
  // specialized pointer analysis
  //
  // ///////////////////////////////////////////////////////////////////////////

  public static class JSPointerFlowGraph extends AstPointerFlowGraph {

    public static class JSPointerFlowVisitor extends AstPointerFlowVisitor implements XInstructionVisitor {
      public JSPointerFlowVisitor(PointerAnalysis pa, CallGraph cg, Graph<PointerKey> delegate, CGNode node, IR ir, BasicBlock bb) {
        super(pa, cg, delegate, node, ir, bb);
      }

      public void visitJavaScriptInvoke(JavaScriptInvoke instruction) {

      }

      public void visitJavaScriptPropertyRead(JavaScriptPropertyRead instruction) {

      }

      public void visitJavaScriptPropertyWrite(JavaScriptPropertyWrite instruction) {

      }

      public void visitTypeOf(JavaScriptTypeOfInstruction instruction) {

      }
    }

    protected JSPointerFlowGraph(PointerAnalysis pa, CallGraph cg) {
      super(pa, cg);
    }

    protected InstructionVisitor makeInstructionVisitor(CGNode node, IR ir, BasicBlock bb) {
      return new JSPointerFlowVisitor(pa, cg, delegate, node, ir, bb);
    }
  }

  public PointerFlowGraphFactory getPointerFlowGraphFactory() {
    return new PointerFlowGraphFactory() {
      public PointerFlowGraph make(PointerAnalysis pa, CallGraph cg) {
        return new JSPointerFlowGraph(pa, cg);
      }
    };
  }

  public static class JSPointerAnalysisImpl extends AstPointerAnalysisImpl {

    JSPointerAnalysisImpl(PropagationCallGraphBuilder builder, CallGraph cg, PointsToMap pointsToMap,
        MutableMapping<InstanceKey> instanceKeys, PointerKeyFactory pointerKeys, InstanceKeyFactory iKeyFactory) {
      super(builder, cg, pointsToMap, instanceKeys, pointerKeys, iKeyFactory);
    }

    public static class JSImplicitPointsToSetVisitor extends AstImplicitPointsToSetVisitor implements
        XInstructionVisitor {
      public JSImplicitPointsToSetVisitor(AstPointerAnalysisImpl analysis, LocalPointerKey lpk) {
        super(analysis, lpk);
      }

      public void visitJavaScriptInvoke(JavaScriptInvoke instruction) {

      }

      public void visitTypeOf(JavaScriptTypeOfInstruction instruction) {

      }

      public void visitJavaScriptPropertyRead(JavaScriptPropertyRead instruction) {

      }

      public void visitJavaScriptPropertyWrite(JavaScriptPropertyWrite instruction) {

      }
    };

    protected ImplicitPointsToSetVisitor makeImplicitPointsToVisitor(LocalPointerKey lpk) {
      return new JSImplicitPointsToSetVisitor(this, lpk);
    }
  };

  protected PropagationSystem makeSystem(AnalysisOptions options) {
    return new PropagationSystem(callGraph, pointerKeyFactory, instanceKeyFactory, options.getSupportRefinement()) {
      public PointerAnalysis makePointerAnalysis(PropagationCallGraphBuilder builder) {
        return new JSPointerAnalysisImpl(builder, cg, pointsToMap, instanceKeys, pointerKeyFactory, instanceKeyFactory);
      }
    };
  }

  // ///////////////////////////////////////////////////////////////////////////
  //
  // IR visitor specialization for JavaScript
  //
  // ///////////////////////////////////////////////////////////////////////////

  protected ConstraintVisitor makeVisitor(ExplicitCallGraph.ExplicitNode node) {
    return new JSConstraintVisitor(this, node);
  }

  public static class JSConstraintVisitor extends AstConstraintVisitor implements XInstructionVisitor {

    public JSConstraintVisitor(AstSSAPropagationCallGraphBuilder builder, ExplicitCallGraph.ExplicitNode node) {
      super(builder, node);
    }

    public void visitUnaryOp(SSAUnaryOpInstruction inst) {
      if (inst.getOpcode() == UnaryOpInstruction.Operator.NEG) {
        int lval = inst.getDef(0);
        PointerKey lk = getPointerKeyForLocal(lval);

        IClass bool = getClassHierarchy().lookupClass(JavaScriptTypes.Boolean);
        InstanceKey key = new ConcreteTypeKey(bool);

        system.newConstraint(lk, key);
      }
    }

    public void visitIsDefined(AstIsDefinedInstruction inst) {
      int lval = inst.getDef(0);
      PointerKey lk = getPointerKeyForLocal(lval);

      IClass bool = getClassHierarchy().lookupClass(JavaScriptTypes.Boolean);
      InstanceKey key = new ConcreteTypeKey(bool);

      system.newConstraint(lk, key);
    }

    public void visitEachElementHasNext(EachElementHasNextInstruction inst) {
      int lval = inst.getDef(0);
      PointerKey lk = getPointerKeyForLocal(lval);

      IClass bool = getClassHierarchy().lookupClass(JavaScriptTypes.Boolean);
      InstanceKey key = new ConcreteTypeKey(bool);

      system.newConstraint(lk, key);
    }

    public void visitTypeOf(JavaScriptTypeOfInstruction instruction) {
      int lval = instruction.getDef(0);
      PointerKey lk = getPointerKeyForLocal(lval);

      IClass string = getClassHierarchy().lookupClass(JavaScriptTypes.String);
      InstanceKey key = new ConcreteTypeKey(string);

      system.newConstraint(lk, key);
    }

    public void visitBinaryOp(final SSABinaryOpInstruction instruction) {
      handleBinaryOp(instruction, node, symbolTable, du);
    }

    public void visitJavaScriptPropertyRead(JavaScriptPropertyRead instruction) {
      newFieldRead(node, instruction.getUse(0), instruction.getUse(1), instruction.getDef(0));
    }

    public void visitJavaScriptPropertyWrite(JavaScriptPropertyWrite instruction) {
      newFieldWrite(node, instruction.getUse(0), instruction.getUse(1), instruction.getUse(2));
    }

    public void visitJavaScriptInvoke(JavaScriptInvoke instruction) {
      PointerKey F = getPointerKeyForLocal(instruction.getFunction());

      InstanceKey[][] consts = computeInvariantParameters(instruction);

      PointerKey uniqueCatch = null;
      if (hasUniqueCatchBlock(instruction, ir)) {
        uniqueCatch = getBuilder().getUniqueCatchKey(instruction, ir, node);
      }

      if (contentsAreInvariant(symbolTable, du, instruction.getFunction())) {
        system.recordImplicitPointsToSet(F);
        InstanceKey[] ik = getInvariantContents(instruction.getFunction());
        for (int i = 0; i < ik.length; i++) {
          system.findOrCreateIndexForInstanceKey(ik[i]);
          CGNode n = getTargetForCall(node, instruction.getCallSite(), ik[i]);
          if (n != null) {
            processJSCall(node, instruction, n, ik[i], consts, uniqueCatch);
          }
        }
      } else {
        system.newSideEffect(new JSDispatchOperator(instruction, node, consts, uniqueCatch), F);
      }
    }

    // ///////////////////////////////////////////////////////////////////////////
    //
    // function call handling
    //
    // ///////////////////////////////////////////////////////////////////////////

    class JSDispatchOperator extends UnaryOperator {
      private final JavaScriptInvoke call;

      private final ExplicitCallGraph.ExplicitNode node;

      private final InstanceKey[][] constParams;

      private final PointerKey uniqueCatch;

      public boolean isLoadOperator() {
        return false;
      }

      public String toString() {
        return "JSDispatch of " + call.getCallSite();
      }

      public int hashCode() {
        return call.getCallSite().hashCode() * node.hashCode();
      }

      public boolean equals(Object o) {
        return (o instanceof JSDispatchOperator) && ((JSDispatchOperator) o).node.equals(node)
            && ((JSDispatchOperator) o).call.equals(call);
      }

      /**
       * @param call
       * @param node
       */
      JSDispatchOperator(JavaScriptInvoke call, ExplicitCallGraph.ExplicitNode node, InstanceKey[][] constParams,
          PointerKey uniqueCatch) {
        this.call = call;
        this.node = node;
        this.constParams = constParams;
        this.uniqueCatch = uniqueCatch;
      }

      private MutableIntSet previousReceivers = IntSetUtil.getDefaultIntSetFactory().make();

      public byte evaluate(IVariable lhs, IVariable rhs) {
        final IntSetVariable receivers = (IntSetVariable) rhs;
        if (receivers.getValue() != null) {
          receivers.getValue().foreachExcluding(previousReceivers, new IntSetAction() {
            public void act(int ptr) {
              InstanceKey iKey = system.getInstanceKey(ptr);
              CGNode target = getTargetForCall(node, call.getSite(), iKey);
              if (target != null) {
                processJSCall(node, call, target, iKey, constParams, uniqueCatch);
                if (!getBuilder().haveAlreadyVisited(target)) {
                  getBuilder().markDiscovered(target);
                }
              }
            }
          });

          previousReceivers.addAll(receivers.getValue());
        }

        return NOT_CHANGED;
      };
    }

    @SuppressWarnings("deprecation")
    void processJSCall(CGNode caller, JavaScriptInvoke instruction, CGNode target, InstanceKey function,
        InstanceKey constParams[][], PointerKey uniqueCatchKey) {
      caller.addTarget(instruction.getCallSite(), target);
      if (!getBuilder().haveAlreadyVisited(target)) {
        getBuilder().markDiscovered(target);
      }

      IR sourceIR = getBuilder().getCFAContextInterpreter().getIR(caller);
      SymbolTable sourceST = sourceIR.getSymbolTable();

      IR targetIR = getBuilder().getCFAContextInterpreter().getIR(target);
      SymbolTable targetST = targetIR.getSymbolTable();

      int av = -1;
      for (int v = 0; v < targetST.getMaxValueNumber(); v++) {
        String[] vns = targetIR.getLocalNames(1, v);
        for (int n = 0; vns != null && n < vns.length; n++) {
          if ("arguments".equals(vns[n])) {
            av = v;
            break;
          }
        }
      }

      int paramCount = targetST.getParameterValueNumbers().length;
      int argCount = instruction.getNumberOfParameters();

      // pass actual arguments to formals in the normal way
      for (int i = 0; i < Math.min(paramCount, argCount); i++) {
        int fn = targetST.getConstant(i);
        PointerKey F = (i == 0) ? getBuilder().getFilteredPointerKeyForLocal(target, targetST.getParameter(i), function)
            : getBuilder().getPointerKeyForLocal(target, targetST.getParameter(i));

        if (constParams != null && constParams[i] != null) {
          for (int j = 0; j < constParams[i].length; j++) {
            system.newConstraint(F, constParams[i][j]);
          }

          if (av != -1)
            newFieldWrite(target, av, fn, constParams[i]);

        } else {
          PointerKey A = getBuilder().getPointerKeyForLocal(caller, instruction.getUse(i));
          system.newConstraint(F, (F instanceof FilteredPointerKey) ? (UnaryOperator) getBuilder().filterOperator
              : (UnaryOperator) assignOperator, A);

          if (av != -1)
            newFieldWrite(target, av, fn, F);
        }
      }

      // extra actual arguments get assigned into the ``arguments'' object
      if (paramCount < argCount) {
        if (av != -1) {
          for (int i = paramCount; i < argCount; i++) {
            int fn = targetST.getConstant(i);
            if (constParams != null && constParams[i] != null) {
              newFieldWrite(target, av, fn, constParams[i]);
            } else {
              PointerKey A = getBuilder().getPointerKeyForLocal(caller, instruction.getUse(i));
              newFieldWrite(target, av, fn, A);
            }
          }
        }
      }

      // extra formal parameters get null (extra args are ignored here)
      else if (argCount < paramCount) {
        int nullvn = sourceST.getNullConstant();
        DefUse sourceDU = getBuilder().getCFAContextInterpreter().getDU(caller);
        InstanceKey[] nullkeys = getInvariantContents(sourceST, sourceDU, caller, nullvn);
        for (int i = argCount; i < paramCount; i++) {
          PointerKey F = getBuilder().getPointerKeyForLocal(target, targetST.getParameter(i));
          for (int k = 0; k < nullkeys.length; k++) {
            system.newConstraint(F, nullkeys[k]);
          }
        }
      }

      // write `length' in argument objects
      if (av != -1) {
        int svn = targetST.getConstant(argCount);
        int lnv = targetST.getConstant("length");
        newFieldWrite(target, av, lnv, svn);
      }

      // return values
      if (instruction.getDef(0) != -1) {
        PointerKey RF = getBuilder().getPointerKeyForReturnValue(target);
        PointerKey RA = getBuilder().getPointerKeyForLocal(caller, instruction.getDef(0));
        system.newConstraint(RA, assignOperator, RF);
      }

      PointerKey EF = getBuilder().getPointerKeyForExceptionalReturnValue(target);
      if (SHORT_CIRCUIT_SINGLE_USES && uniqueCatchKey != null) {
        // e has exactly one use. so, represent e implicitly
        system.newConstraint(uniqueCatchKey, assignOperator, EF);
      } else {
        PointerKey EA = getBuilder().getPointerKeyForLocal(caller, instruction.getDef(1));
        system.newConstraint(EA, assignOperator, EF);
      }
    }

    // ///////////////////////////////////////////////////////////////////////////
    //
    // string manipulation handling for binary operators
    //
    // ///////////////////////////////////////////////////////////////////////////

    private void handleBinaryOp(final SSABinaryOpInstruction instruction, final CGNode node, final SymbolTable symbolTable,
        final DefUse du) {
      int lval = instruction.getDef(0);
      PointerKey lk = getPointerKeyForLocal(lval);
      final PointsToSetVariable lv = system.findOrCreatePointsToSet(lk);

      final int arg1 = instruction.getUse(0);
      final int arg2 = instruction.getUse(1);

      class BinaryOperator extends AbstractOperator {

        private CGNode getNode() {
          return node;
        }

        private SSABinaryOpInstruction getInstruction() {
          return instruction;
        }

        public String toString() {
          return "BinOp: " + getInstruction();
        }

        public int hashCode() {
          return 17 * getInstruction().getUse(0) * getInstruction().getUse(1);
        }

        public boolean equals(Object o) {
          if (o instanceof BinaryOperator) {
            BinaryOperator op = (BinaryOperator) o;
            return op.getNode().equals(getNode()) && op.getInstruction().getUse(0) == getInstruction().getUse(0)
                && op.getInstruction().getUse(1) == getInstruction().getUse(1)
                && op.getInstruction().getDef(0) == getInstruction().getDef(0);
          } else {
            return false;
          }
        }

        private InstanceKey[] getInstancesArray(int vn) {
          if (contentsAreInvariant(symbolTable, du, vn)) {
            return getInvariantContents(vn);
          } else {
            PointsToSetVariable v = system.findOrCreatePointsToSet(getPointerKeyForLocal(vn));
            if (v.getValue() == null || v.size() == 0) {
              return new InstanceKey[0];
            } else {
              final Set<InstanceKey> temp = HashSetFactory.make();
              v.getValue().foreach(new IntSetAction() {
                public void act(int keyIndex) {
                  temp.add(system.getInstanceKey(keyIndex));
                }
              });

              return temp.toArray(new InstanceKey[temp.size()]);
            }
          }
        }

        private boolean isStringConstant(InstanceKey k) {
          return (k instanceof ConstantKey) && k.getConcreteType().getReference().equals(JavaScriptTypes.String);
        }

        private boolean addKey(InstanceKey k) {
          int n = system.findOrCreateIndexForInstanceKey(k);
          if (!lv.contains(n)) {
            lv.add(n);
            return true;
          } else {
            return false;
          }
        }

        public byte evaluate(IVariable lhs, final IVariable[] rhs) {
          boolean doDefault = false;
          byte changed = NOT_CHANGED;

          InstanceKey[] iks1 = getInstancesArray(arg1);
          InstanceKey[] iks2 = getInstancesArray(arg2);

          if ((instruction.getOperator() == BinaryOpInstruction.Operator.ADD) && (getOptions().getTraceStringConstants())) {
            for (int i = 0; i < iks1.length; i++) {
              if (isStringConstant(iks1[i])) {
                for (int j = 0; j < iks2.length; j++) {
                  if (isStringConstant(iks2[j])) {
                    String v1 = (String) ((ConstantKey) iks1[i]).getValue();
                    String v2 = (String) ((ConstantKey) iks2[j]).getValue();
                    if (v1.indexOf(v2) == -1 && v2.indexOf(v1) == -1) {
                      InstanceKey lvalKey = getInstanceKeyForConstant(v1 + v2);
                      if (addKey(lvalKey)) {
                        changed = CHANGED;
                      }
                    } else {
                      doDefault = true;
                    }
                  } else {
                    doDefault = true;
                  }
                }
              } else {
                doDefault = true;
              }
            }
          } else {
            doDefault = true;
          }

          if (doDefault) {
            for (int i = 0; i < iks1.length; i++) {
              if (addKey(new ConcreteTypeKey(iks1[i].getConcreteType()))) {
                changed = CHANGED;
              }
            }
            for (int i = 0; i < iks2.length; i++) {
              if (addKey(new ConcreteTypeKey(iks2[i].getConcreteType()))) {
                changed = CHANGED;
              }
            }
          }

          return changed;
        }
      }

      BinaryOperator B = new BinaryOperator();
      if (contentsAreInvariant(symbolTable, du, arg1)) {
        if (contentsAreInvariant(symbolTable, du, arg2)) {
          B.evaluate(null, null);
        } else {
          system.newConstraint(lk, B, getPointerKeyForLocal(arg2));
        }
      } else {
        PointerKey k1 = getPointerKeyForLocal(arg1);
        if (contentsAreInvariant(symbolTable, du, arg2)) {
          system.newConstraint(lk, B, k1);
        } else {
          system.newConstraint(lk, B, k1, getPointerKeyForLocal(arg2));
        }
      }
    }
  }

}
