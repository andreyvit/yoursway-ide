package com.yoursway.ruby.wala;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.ibm.wala.cast.types.AstMethodReference;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.classLoader.NewSiteReference;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.MethodTargetSelector;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ipa.summaries.MethodSummary;
import com.ibm.wala.ssa.ConstantValue;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAAbstractInvokeInstruction;
import com.ibm.wala.ssa.SSAInstructionFactory;
import com.ibm.wala.ssa.SymbolTable;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeName;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.collections.HashMapFactory;
import com.ibm.wala.util.collections.Pair;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.debug.Trace;
import com.yoursway.js.wala.JavaScriptPropertyRead;
import com.yoursway.js.wala.JavaScriptPropertyWrite;
import com.yoursway.ruby.wala.classloader.RubyClassLoader;

public class RubyConstructTargetSelector implements MethodTargetSelector {
    private static final boolean DEBUG = false;
    
    private final IClassHierarchy cha;
    
    private final MethodTargetSelector base;
    
    private final Map<Object, IMethod> constructors = HashMapFactory.make();
    
    private class JavaScriptConstructor extends RubySummarizedFunction {
        private final String toStringExtra;
        
        private JavaScriptConstructor(MethodReference ref, MethodSummary summary, IClass declaringClass,
                String toStringExtra) {
            super(ref, summary, declaringClass);
            this.toStringExtra = toStringExtra;
        }
        
        private JavaScriptConstructor(MethodReference ref, MethodSummary summary, IClass declaringClass) {
            this(ref, summary, declaringClass, "");
        }
        
        public String toString() {
            return "<ctor for " + getReference().getDeclaringClass() + toStringExtra + ">";
        }
    }
    
    public RubyConstructTargetSelector(IClassHierarchy cha, MethodTargetSelector base) {
        this.cha = cha;
        this.base = base;
    }
    
    private IMethod record(Object key, IMethod m) {
        constructors.put(key, m);
        return m;
    }
    
    private IMethod makeNullaryValueConstructor(IClass cls, Object value) {
        MethodReference ref = RubyMethods.makeCtorReference(cls.getReference());
        RubySummary S = new RubySummary(ref, 1);
        
        S.addStatement(new RubyStaticPropertyRead(4, 1, "prototype"));
        
        S.addStatement(new RubyNewInstruction(5, NewSiteReference.make(S.getNextProgramCounter(), cls
                .getReference())));
        
        S.addStatement(new RubyStaticPropertyWrite(5, "prototype", 4));
        
        S.addConstant(new Integer(8), new ConstantValue(value));
        S.addStatement(new RubyStaticPropertyWrite(5, "$value", 8));
        
        S.addStatement(SSAInstructionFactory.ReturnInstruction(5, false));
        
        return new JavaScriptConstructor(ref, S, cls);
    }
    
    private IMethod makeUnaryValueConstructor(IClass cls) {
        MethodReference ref = RubyMethods.makeCtorReference(cls.getReference());
        RubySummary S = new RubySummary(ref, 2);
        
        S.addStatement(new RubyStaticPropertyRead(5, 1, "prototype"));
        
        S.addStatement(new RubyNewInstruction(6, NewSiteReference.make(S.getNextProgramCounter(), cls
                .getReference())));
        
        S.addStatement(new RubyStaticPropertyWrite(6, "prototype", 5));
        
        S.addStatement(new RubyStaticPropertyWrite(6, "$value", 2));
        
        S.addStatement(SSAInstructionFactory.ReturnInstruction(6, false));
        
        return new JavaScriptConstructor(ref, S, cls);
    }
    
    private IMethod makeValueConstructor(IClass cls, int nargs, Object value) {
        Assertions._assert(nargs == 0 || nargs == 1);
        
        Object key = new Pair<IClass, Integer>(cls, new Integer(nargs));
        if (constructors.containsKey(key))
            return constructors.get(key);
        
        else
            return record(key, (nargs == 0) ? makeNullaryValueConstructor(cls, value)
                    : makeUnaryValueConstructor(cls));
    }
    
    private IMethod makeNullaryObjectConstructor(IClass cls) {
        MethodReference ref = RubyMethods.makeCtorReference(RubyTypes.Object);
        RubySummary S = new RubySummary(ref, 1);
        
        S.addStatement(new RubyStaticPropertyRead(4, 1, "prototype"));
        
        S.addStatement(new RubyNewInstruction(5, NewSiteReference.make(S.getNextProgramCounter(),
                RubyTypes.Object)));
        
        S.addStatement(new RubyStaticPropertyWrite(5, "prototype", 4));
        
        S.addStatement(SSAInstructionFactory.ReturnInstruction(5, false));
        
        return new JavaScriptConstructor(ref, S, cls);
    }
    
    private IMethod makeUnaryObjectConstructor(IClass cls) {
        MethodReference ref = RubyMethods.makeCtorReference(RubyTypes.Object);
        RubySummary S = new RubySummary(ref, 2);
        
        S.addStatement(SSAInstructionFactory.ReturnInstruction(2, false));
        
        return new JavaScriptConstructor(ref, S, cls);
    }
    
    private IMethod makeObjectConstructor(IClass cls, int nargs) {
        Assertions._assert(nargs == 0 || nargs == 1);
        
        Object key = new Pair<IClass, Integer>(cls, new Integer(nargs));
        if (constructors.containsKey(key))
            return constructors.get(key);
        
        else
            return record(key, (nargs == 0) ? makeNullaryObjectConstructor(cls)
                    : makeUnaryObjectConstructor(cls));
    }
    
    private IMethod makeObjectCall(IClass cls, int nargs) {
        Assertions._assert(nargs == 0);
        
        Object key = new Pair<IClass, Integer>(cls, new Integer(nargs));
        if (constructors.containsKey(key))
            return constructors.get(key);
        
        else
            return record(key, makeNullaryObjectConstructor(cls));
    }
    
    private IMethod makeArrayLengthConstructor(IClass cls) {
        MethodReference ref = RubyMethods.makeCtorReference(RubyTypes.Array);
        RubySummary S = new RubySummary(ref, 2);
        
        S.addStatement(new RubyStaticPropertyRead(5, 1, "prototype"));
        
        S.addStatement(new RubyNewInstruction(6, NewSiteReference.make(S.getNextProgramCounter(),
                RubyTypes.Array)));
        
        S.addStatement(new RubyStaticPropertyWrite(6, "prototype", 5));
        
        S.addStatement(new RubyStaticPropertyWrite(6, "length", 2));
        
        S.addStatement(SSAInstructionFactory.ReturnInstruction(6, false));
        
        return new JavaScriptConstructor(ref, S, cls);
    }
    
    private IMethod makeArrayContentsConstructor(IClass cls, int nargs) {
        MethodReference ref = RubyMethods.makeCtorReference(RubyTypes.Array);
        RubySummary S = new RubySummary(ref, nargs + 1);
        
        S.addConstant(new Integer(nargs + 3), new ConstantValue("prototype"));
        S.addStatement(new JavaScriptPropertyRead(nargs + 4, 1, nargs + 3));
        
        S.addStatement(new RubyNewInstruction(nargs + 5, NewSiteReference.make(S.getNextProgramCounter(),
                RubyTypes.Array)));
        
        S.addStatement(new RubyStaticPropertyWrite(nargs + 5, "prototype", nargs + 4));
        
        S.addConstant(new Integer(nargs + 7), new ConstantValue(nargs));
        S.addStatement(new RubyStaticPropertyWrite(nargs + 5, "length", nargs + 7));
        
        int vn = nargs + 9;
        for (int i = 0; i < nargs; i++, vn += 2) {
            S.addConstant(new Integer(vn), new ConstantValue(i));
            S.addStatement(new JavaScriptPropertyWrite(nargs + 5, vn, i + 1));
        }
        
        S.addStatement(SSAInstructionFactory.ReturnInstruction(5, false));
        
        return new JavaScriptConstructor(ref, S, cls);
    }
    
    private IMethod makeArrayConstructor(IClass cls, int nargs) {
        Object key = new Pair<IClass, Integer>(cls, new Integer(nargs));
        if (constructors.containsKey(key))
            return constructors.get(key);
        
        else
            return record(key, (nargs == 1) ? makeArrayLengthConstructor(cls) : makeArrayContentsConstructor(
                    cls, nargs));
    }
    
    private IMethod makeNullaryStringCall(IClass cls) {
        MethodReference ref = AstMethodReference.fnReference(RubyTypes.String);
        RubySummary S = new RubySummary(ref, 1);
        
        S.addConstant(new Integer(2), new ConstantValue(""));
        S.addStatement(SSAInstructionFactory.ReturnInstruction(2, false));
        
        return new JavaScriptConstructor(ref, S, cls);
    }
    
    private IMethod makeUnaryStringCall(IClass cls) {
        MethodReference ref = AstMethodReference.fnReference(RubyTypes.String);
        RubySummary S = new RubySummary(ref, 2);
        
        S.addStatement(new RubyStaticPropertyRead(4, 2, "toString"));
        
        CallSiteReference cs = new RubyCallSiteReference(S.getNextProgramCounter());
        S.addStatement(new RubyInvoke(4, 5, new int[] { 2 }, 6, cs));
        
        S.addStatement(SSAInstructionFactory.ReturnInstruction(5, false));
        
        return new JavaScriptConstructor(ref, S, cls);
    }
    
    private IMethod makeStringCall(IClass cls, int nargs) {
        Assertions._assert(nargs == 0 || nargs == 1);
        
        Object key = new Pair<IClass, Integer>(cls, new Integer(nargs));
        if (constructors.containsKey(key))
            return constructors.get(key);
        
        else
            return record(key, (nargs == 0) ? makeNullaryStringCall(cls) : makeUnaryStringCall(cls));
    }
    
    private IMethod makeNullaryNumberCall(IClass cls) {
        MethodReference ref = AstMethodReference.fnReference(RubyTypes.Number);
        RubySummary S = new RubySummary(ref, 1);
        
        S.addConstant(new Integer(2), new ConstantValue(0.0));
        S.addStatement(SSAInstructionFactory.ReturnInstruction(2, false));
        
        return new JavaScriptConstructor(ref, S, cls);
    }
    
    private IMethod makeUnaryNumberCall(IClass cls) {
        MethodReference ref = AstMethodReference.fnReference(RubyTypes.Number);
        RubySummary S = new RubySummary(ref, 2);
        
        S.addStatement(new RubyStaticPropertyRead(4, 2, "toNumber"));
        
        CallSiteReference cs = new RubyCallSiteReference(S.getNextProgramCounter());
        S.addStatement(new RubyInvoke(4, 5, new int[] { 2 }, 6, cs));
        
        S.addStatement(SSAInstructionFactory.ReturnInstruction(5, false));
        
        return new JavaScriptConstructor(ref, S, cls);
    }
    
    private IMethod makeNumberCall(IClass cls, int nargs) {
        Assertions._assert(nargs == 0 || nargs == 1);
        
        Object key = new Pair<IClass, Integer>(cls, new Integer(nargs));
        if (constructors.containsKey(key))
            return constructors.get(key);
        
        else
            return record(key, (nargs == 0) ? makeNullaryNumberCall(cls) : makeUnaryNumberCall(cls));
    }
    
    private IMethod makeFunctionConstructor(IClass receiver, IClass cls) {
        Pair<IClass, IClass> tableKey = new Pair<IClass, IClass>(receiver, cls);
        if (constructors.containsKey(tableKey))
            return constructors.get(tableKey);
        
        MethodReference ref = RubyMethods.makeCtorReference(receiver.getReference());
        RubySummary S = new RubySummary(ref, 1);
        
        S.addStatement(new RubyStaticPropertyRead(4, 1, "prototype"));
        
        S.addStatement(new RubyNewInstruction(5, NewSiteReference.make(S.getNextProgramCounter(), cls
                .getReference())));
        
        S.addStatement(new RubyStaticPropertyWrite(5, "prototype", 4));
        
        S.addStatement(new RubyNewInstruction(7, NewSiteReference.make(S.getNextProgramCounter(),
                RubyTypes.Object)));
        
        S.addStatement(new RubyStaticPropertyWrite(5, "prototype", 7));
        
        S.addStatement(new RubyStaticPropertyWrite(7, "constructor", 5));
        
        S.addStatement(SSAInstructionFactory.ReturnInstruction(5, false));
        
        if (receiver != cls)
            return record(tableKey, new JavaScriptConstructor(ref, S, receiver, "("
                    + cls.getReference().getName() + ")"));
        else
            return record(tableKey, new JavaScriptConstructor(ref, S, receiver));
    }
    
    private int ctorCount = 0;
    
    private IMethod makeFunctionConstructor(IR callerIR, SSAAbstractInvokeInstruction callStmt, IClass cls,
            int nargs) {
        SymbolTable ST = callerIR.getSymbolTable();
        
        if (nargs == 0) {
            return makeFunctionConstructor(cls, cls);
        } else if (nargs == 1) {
            if (ST.isStringConstant(callStmt.getUse(1))) {
                TypeReference ref = TypeReference.findOrCreate(RubyTypes.rubyLoader, TypeName
                        .string2TypeName((String) ST.getStringValue(callStmt.getUse(1))));
                
                if (DEBUG) {
                    Trace.println("ctor type name is " + (String) ST.getStringValue(callStmt.getUse(1)));
                }
                
                IClass cls2 = cha.lookupClass(ref);
                if (cls2 != null) {
                    return makeFunctionConstructor(cls, cls2);
                }
            }
            
            return makeFunctionConstructor(cls, cls);
        } else {
            Assertions._assert(nargs > 1);
            RubyClassLoader cl = (RubyClassLoader) cha.getLoader(RubyTypes.rubyLoader);
            
            for (int i = 1; i < callStmt.getNumberOfUses(); i++)
                if (!ST.isStringConstant(callStmt.getUse(i)))
                    return makeFunctionConstructor(cls, cls);
            
            StringBuffer fun = new StringBuffer("function _fromctor (");
            for (int j = 1; j < callStmt.getNumberOfUses() - 1; j++) {
                if (j != 1)
                    fun.append(",");
                fun.append(ST.getStringValue(callStmt.getUse(j)));
            }
            
            fun.append(") {");
            fun.append(ST.getStringValue(callStmt.getUse(callStmt.getNumberOfUses() - 1)));
            fun.append("}");
            
            try {
                String fileName = "ctor " + ++ctorCount;
                File f = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
                FileWriter FO = new FileWriter(f);
                FO.write(fun.toString());
                FO.close();
//                (UtilCG.getJavaScriptTranslatorFactory().make(cl)).translate(new SourceFileModule(f, System
//                        .getProperty("java.io.tmpdir")), fileName);
                f.delete();
                IClass fcls = cl.lookupClass("Lctor " + ctorCount + "/_fromctor", cha);
                cha.addClass(fcls);
                
                if (DEBUG)
                    Trace.println("looking for ctor " + ctorCount + " and got " + fcls);
                
                if (fcls != null)
                    return makeFunctionConstructor(cls, fcls);
                
            } catch (IOException e) {
                
            }
            
            return makeFunctionConstructor(cls, cls);
        }
    }
    
    private IMethod makeFunctionObjectConstructor(IClass cls, int nargs) {
        Object key = new Pair<IClass, Integer>(cls, new Integer(nargs));
        if (constructors.containsKey(key))
            return constructors.get(key);
        
        MethodReference ref = RubyMethods.makeCtorReference(cls.getReference());
        RubySummary S = new RubySummary(ref, nargs + 1);
        
        S.addStatement(new RubyStaticPropertyRead(nargs + 4, 1, "prototype"));
        
        S.addStatement(new RubyNewInstruction(nargs + 5, NewSiteReference.make(S
                .getNextProgramCounter(), RubyTypes.Object)));
        
        S.addStatement(new RubyStaticPropertyWrite(nargs + 5, "prototype", nargs + 4));
        
        CallSiteReference cs = new RubyCallSiteReference(S.getNextProgramCounter());
        int[] args = new int[nargs + 1];
        args[0] = nargs + 5;
        for (int i = 0; i < nargs; i++)
            args[i + 1] = i + 2;
        S.addStatement(new RubyInvoke(1, nargs + 7, args, nargs + 8, cs));
        
        S.addStatement(SSAInstructionFactory.ReturnInstruction(nargs + 7, false));
        
        S.addStatement(SSAInstructionFactory.ReturnInstruction(nargs + 5, false));
        
        return record(key, new JavaScriptConstructor(ref, S, cls));
    }
    
    private IMethod findOrCreateConstructorMethod(IR callerIR, SSAAbstractInvokeInstruction callStmt,
            IClass receiver, int nargs) {
        if (receiver.getReference().equals(RubyTypes.Object))
            return makeObjectConstructor(receiver, nargs);
        else if (receiver.getReference().equals(RubyTypes.Array))
            return makeArrayConstructor(receiver, nargs);
        else if (receiver.getReference().equals(RubyTypes.StringObject))
            return makeValueConstructor(receiver, nargs, "");
        else if (receiver.getReference().equals(RubyTypes.BooleanObject)) {
            Assertions._assert(nargs == 1);
            return makeValueConstructor(receiver, nargs, null);
        } else if (receiver.getReference().equals(RubyTypes.NumberObject))
            return makeValueConstructor(receiver, nargs, new Integer(0));
        else if (receiver.getReference().equals(RubyTypes.Function))
            return makeFunctionConstructor(callerIR, callStmt, receiver, nargs);
        else if (cha.isSubclassOf(receiver, cha.lookupClass(RubyTypes.CodeBody)))
            return makeFunctionObjectConstructor(receiver, nargs);
        
        else {
            return null;
        }
    }
    
    @SuppressWarnings("unused")
    private IMethod findOrCreateCallMethod(IR callerIR, SSAAbstractInvokeInstruction callStmt,
            IClass receiver, int nargs) {
        if (receiver.getReference().equals(RubyTypes.Object))
            return makeObjectCall(receiver, nargs);
        else if (receiver.getReference().equals(RubyTypes.Array))
            return makeArrayConstructor(receiver, nargs);
        else if (receiver.getReference().equals(RubyTypes.StringObject))
            return makeStringCall(receiver, nargs);
        else if (receiver.getReference().equals(RubyTypes.NumberObject))
            return makeNumberCall(receiver, nargs);
//        else if (receiver.getReference().equals(RubyTypes.Function))
//            return makeFunctionConstructor(callerIR, callStmt, receiver, nargs);
        else {
            return null;
        }
    }
    
    public IMethod getCalleeTarget(CGNode caller, CallSiteReference site, IClass receiver) {
        if (site.getDeclaredTarget().equals(RubyMethods.ctorReference)) {
            Assertions._assert(cha.isSubclassOf(receiver, cha.lookupClass(RubyTypes.Root)));
            IR callerIR = caller.getIR();
            SSAAbstractInvokeInstruction callStmts[] = callerIR.getCalls(site);
            Assertions._assert(callStmts.length == 1);
            int nargs = callStmts[0].getNumberOfUses();
            return findOrCreateConstructorMethod(callerIR, callStmts[0], receiver, nargs - 1);
        } else {
            return base.getCalleeTarget(caller, site, receiver);
        }
    }
    
    public boolean mightReturnSyntheticMethod(CGNode caller, CallSiteReference site) {
        return true;
    }
    
    public boolean mightReturnSyntheticMethod(MethodReference declaredTarget) {
        return true;
    }
}
