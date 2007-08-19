package com.yoursway.ruby.wala;

import com.ibm.wala.cast.ir.ssa.AssignInstruction;
import com.ibm.wala.cast.ir.ssa.AstIsDefinedInstruction;
import com.ibm.wala.cast.ir.ssa.NonExceptingThrowInstruction;
import com.ibm.wala.cast.ir.translator.AstTranslator;
import com.ibm.wala.cast.loader.AstMethod.DebuggingInformation;
import com.ibm.wala.cast.loader.AstMethod.LexicalInformation;
import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.ibm.wala.cast.tree.CAstSymbol;
import com.ibm.wala.cast.tree.CAstType;
import com.ibm.wala.cast.tree.impl.CAstSymbolImpl;
import com.ibm.wala.cast.tree.visit.CAstVisitor;
import com.ibm.wala.cast.types.AstMethodReference;
import com.ibm.wala.cfg.AbstractCFG;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.NewSiteReference;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.ssa.SSANewInstruction;
import com.ibm.wala.ssa.SymbolTable;
import com.ibm.wala.types.FieldReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeName;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.Atom;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.debug.Trace;
import com.yoursway.ruby.wala.classloader.RubyClassLoader;
import com.yoursway.ruby.wala.view.AdHocView;

public class RubyCAst2IRTranslator extends AstTranslator {
    private final static boolean DEBUG = true;
    
    public RubyCAst2IRTranslator(RubyClassLoader loader) {
        super(loader);
    }
    
    protected boolean useDefaultInitValues() {
        return false;
    }
    
    protected boolean hasImplicitGlobals() {
        return true;
    }
    
    protected boolean treatGlobalsAsLexicallyScoped() {
        return true;
    }
    
    protected boolean useLocalValuesForLexicalVars() {
        return true;
    }
    
    protected TypeReference defaultCatchType() {
        return RubyTypes.Root;
    }
    
    protected TypeReference makeType(CAstType type) {
        Assertions.UNREACHABLE("JavaScript does not use CAstType");
        return null;
    }
    
    protected void defineType(CAstEntity type, WalkContext wc) {
        Assertions
                .UNREACHABLE("JavaScript doesn't have types. I suggest you look elsewhere for your amusement.");
    }
    
    protected void defineField(CAstEntity topEntity, WalkContext wc, CAstEntity n) {
        Assertions.UNREACHABLE("JavaScript doesn't have fields, numb-nuts!");
    }
    
    protected String composeEntityName(WalkContext parent, CAstEntity f) {
        if (f.getKind() == CAstEntity.SCRIPT_ENTITY)
            return f.getName();
        else
            return parent.getName() + "/" + f.getName();
    }
    
    protected void declareFunction(CAstEntity N, WalkContext context) {
        String fnName = composeEntityName(context, N);
        if (N.getKind() == CAstEntity.SCRIPT_ENTITY) {
            ((RubyClassLoader) loader).defineScriptType("L" + fnName, N.getPosition());
        } else if (N.getKind() == CAstEntity.FUNCTION_ENTITY) {
            ((RubyClassLoader) loader).defineFunctionType("L" + fnName, N.getPosition());
        } else {
            Assertions.UNREACHABLE();
        }
    }
    
    protected void defineFunction(CAstEntity N, WalkContext definingContext, AbstractCFG cfg,
            SymbolTable symtab, boolean hasCatchBlock, TypeReference[][] caughtTypes, LexicalInformation LI,
            DebuggingInformation debugInfo) {
        if (DEBUG)
            Trace.println("\n\nAdding code for " + N);
        String fnName = composeEntityName(definingContext, N);
        
        if (DEBUG)
            Trace.println(cfg);
        
        ((RubyClassLoader) loader).defineCodeBodyCode("L" + fnName, cfg, symtab, hasCatchBlock, caughtTypes,
                LI, debugInfo);
    }
    
    protected void doThrow(WalkContext context, int exception) {
        context.cfg().addInstruction(new NonExceptingThrowInstruction(exception));
    }
    
    protected void doCall(WalkContext context, CAstNode call, int result, int exception, CAstNode name,
            int receiver, int[] arguments) {
        MethodReference ref = name.getValue().equals("ctor") ? RubyMethods.ctorReference : AstMethodReference
                .fnReference(RubyTypes.CodeBody);
        
        // FIXME: missing receiver (was a first arg)
        context.cfg().addInstruction(
                new RubyInvoke(result, arguments, exception, new RubyCallSiteReference(ref, context
                        .cfg().getCurrentInstruction())));
        
        context.cfg().addPreNode(call, context.getUnwindState());
        
        context.cfg().newBlock(true);
        
        if (context.getControlFlow().getTarget(call, null) != null)
            context.cfg().addPreEdge(call, context.getControlFlow().getTarget(call, null), true);
        else
            context.cfg().addPreEdgeToExit(call, true);
    }
    
    protected void doNewObject(WalkContext context, CAstNode newNode, int result, Object type, int[] arguments) {
        Assertions._assert(arguments == null);
        TypeReference typeRef = TypeReference.findOrCreate(RubyTypes.rubyLoader, TypeName.string2TypeName("L"
                + type));
        
        context.cfg().addInstruction(
                new RubyNewInstruction(result, NewSiteReference.make(context.cfg().getCurrentInstruction(),
                        typeRef)));
    }
    
    protected void doMaterializeFunction(WalkContext context, int result, int exception, CAstEntity fn) {
        int nm = context.currentScope().getConstantValue("L" + composeEntityName(context, fn));
        int tmp = doGlobalRead(context, "Function");
        // FIXME: first arg was tmp
        context.cfg().addInstruction(
                new RubyInvoke(result, new int[] { nm }, exception, new RubyCallSiteReference(
                        RubyMethods.ctorReference, context.cfg().getCurrentInstruction())));
    }
    
    public void doArrayRead(WalkContext context, int result, int arrayValue, CAstNode arrayRef,
            int[] dimValues) {
        Assertions.UNREACHABLE("JSAstTranslator.doArrayRead() called!");
    }
    
    public void doArrayWrite(WalkContext context, int arrayValue, CAstNode arrayRef, int[] dimValues, int rval) {
        Assertions.UNREACHABLE("JSAstTranslator.doArrayWrite() called!");
    }
    
    protected void doFieldRead(WalkContext context, int result, int receiver, CAstNode elt, CAstNode parent) {
        Assertions.UNREACHABLE("JSAstTranslator.doFieldRead() called!");
        //    walkNodes(elt, context);
        //    int x = context.currentScope().allocateTempValue();
        //
        //    context.cfg().addInstruction(new AssignInstruction(x, receiver));
        //
        //    if (elt.getKind()==CAstNode.CONSTANT && elt.getValue() instanceof String) {
        //      context.cfg().addInstruction(
        //        new JavaScriptStaticPropertyRead(result, x, (String)elt.getValue()));
        //    } else {
        //      context.cfg().addInstruction(
        //        new JavaScriptPropertyRead(result, x, getValue(elt) ));
        //    }
    }
    
    protected void doFieldWrite(WalkContext context, int receiver, CAstNode elt, CAstNode parent, int rval) {
        Assertions.UNREACHABLE("JSAstTranslator.doFieldWrite() called!");
        //    walkNodes(elt, context);
        //    if (elt.getKind() == CAstNode.CONSTANT && elt.getValue() instanceof String)
        //    {
        //      context.cfg().addInstruction(
        //        new JavaScriptStaticPropertyWrite(receiver, (String)elt.getValue(), rval));
        //    } else {
        //      context.cfg().addInstruction(
        //        new JavaScriptPropertyWrite(receiver, getValue(elt), rval));
        //    }
    }
    
    private void doPrimitiveNew(WalkContext context, int resultVal, String typeName) {
        doNewObject(context, null, resultVal, typeName + "Object", null);
        //    int rval = context.currentScope().getConstantValue(typeName);
        //    context.cfg().addInstruction(
        //      new JavaScriptStaticPropertyWrite(resultVal, "class" , rval));
    }
    
    protected void doPrimitive(int resultVal, WalkContext context, CAstNode primitiveCall) {
        try {
            String name = (String) primitiveCall.getChild(0).getValue();
            if (name.equals("GlobalNaN")) {
                context.cfg().addInstruction(
                        new AssignInstruction(resultVal, context.currentScope().getConstantValue(
                                new Float(Float.NaN))));
            } else if (name.equals("GlobalInfinity")) {
                context.cfg().addInstruction(
                        new AssignInstruction(resultVal, context.currentScope().getConstantValue(
                                new Float(Float.POSITIVE_INFINITY))));
            } else if (name.equals("MathE")) {
                context.cfg().addInstruction(
                        new AssignInstruction(resultVal, context.currentScope().getConstantValue(
                                new Double(Math.E))));
            } else if (name.equals("MathPI")) {
                context.cfg().addInstruction(
                        new AssignInstruction(resultVal, context.currentScope().getConstantValue(
                                new Double(Math.PI))));
            } else if (name.equals("MathSQRT1_2")) {
                context.cfg().addInstruction(
                        new AssignInstruction(resultVal, context.currentScope().getConstantValue(
                                new Double(Math.sqrt(.5)))));
            } else if (name.equals("MathSQRT2")) {
                context.cfg().addInstruction(
                        new AssignInstruction(resultVal, context.currentScope().getConstantValue(
                                new Double(Math.sqrt(2)))));
            } else if (name.equals("MathLN2")) {
                context.cfg().addInstruction(
                        new AssignInstruction(resultVal, context.currentScope().getConstantValue(
                                new Double(Math.log(2)))));
            } else if (name.equals("MathLN10")) {
                context.cfg().addInstruction(
                        new AssignInstruction(resultVal, context.currentScope().getConstantValue(
                                new Double(Math.log(10)))));
            } else if (name.equals("NewObject")) {
                doNewObject(context, null, resultVal, "Object", null);
                
            } else if (name.equals("NewArray")) {
                doNewObject(context, null, resultVal, "Array", null);
                
            } else if (name.equals("NewString")) {
                doPrimitiveNew(context, resultVal, "String");
                
            } else if (name.equals("NewNumber")) {
                doPrimitiveNew(context, resultVal, "Number");
                
            } else if (name.equals("NewRegExp")) {
                doPrimitiveNew(context, resultVal, "RegExp");
                
            } else if (name.equals("NewFunction")) {
                doNewObject(context, null, resultVal, "Function", null);
                
            } else if (name.equals("NewUndefined")) {
                doNewObject(context, null, resultVal, "Undefined", null);
                
            } else {
                context.cfg().addInstruction(
                        new AssignInstruction(resultVal, context.currentScope().getConstantValue(null)));
            }
        } catch (ClassCastException e) {
            throw new RuntimeException("Cannot translate primitive " + primitiveCall.getChild(0).getValue());
        }
    }
    
    protected void doIsFieldDefined(WalkContext context, int result, int ref, CAstNode f) {
        if (f.getKind() == CAstNode.CONSTANT && f.getValue() instanceof String) {
            String field = (String) f.getValue();
            
            FieldReference fieldRef = FieldReference.findOrCreate(RubyTypes.Root, Atom
                    .findOrCreateUnicodeAtom((String) field), RubyTypes.Root);
            
            context.cfg().addInstruction(new AstIsDefinedInstruction(result, ref, fieldRef));
            
        } else {
            
            context.cfg().addInstruction(new AstIsDefinedInstruction(result, ref, getValue(f)));
        }
    }
    
    protected void doPrologue(WalkContext context) {
        super.doPrologue(context);
        //    int tempVal = context.currentScope().allocateTempValue();
        //    doNewObject(context, null, tempVal, "Array", null);
        //    CAstSymbol args = new CAstSymbolImpl("arguments");
        //    context.currentScope().declare(args, tempVal);
        //    context.cfg().addInstruction(
        //      new JavaScriptStaticPropertyWrite(1, "arguments", tempVal));
    }
    
    protected boolean doVisit(CAstNode n, Context cntxt, CAstVisitor visitor) {
        //    WalkContext context = (WalkContext)cntxt;
        //    switch (n.getKind()) {
        //    case CAstNode.TYPE_OF: {
        //      int result = context.currentScope().allocateTempValue();
        //
        //      walkNodes(n.getChild(0), context);
        //      int ref = getValue(n.getChild(0));
        //
        //      context.cfg().addInstruction(
        //        new JavaScriptTypeOfInstruction(result, ref));
        //
        //      setValue(n, result);
        //      return true;
        //    }
        //
        //    default: {
        return false;
        //    }
        //    }
    }
    
    @Override
    public void translate(CAstEntity N, String nm) {
        TestApplication.translatingEntity(N, nm);
        super.translate(N, nm);
    }
    
}
