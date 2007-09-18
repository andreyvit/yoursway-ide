package com.yoursway.ruby.wala2.translator;

import com.ibm.wala.cast.ir.translator.AstTranslator;
import com.ibm.wala.cast.loader.AstMethod.DebuggingInformation;
import com.ibm.wala.cast.loader.AstMethod.LexicalInformation;
import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.ibm.wala.cast.tree.CAstType;
import com.ibm.wala.cast.tree.visit.CAstVisitor;
import com.ibm.wala.cfg.AbstractCFG;
import com.ibm.wala.ssa.SymbolTable;
import com.ibm.wala.types.TypeReference;
import com.yoursway.ruby.wala2.RubyConstants;
import com.yoursway.ruby.wala2.loader.RubyClassLoader;

public class RubyCAst2IRTranslator extends AstTranslator {

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
		return RubyConstants.OBJECT_REF;
	}

	protected TypeReference makeType(CAstType type) {
		throw new UnsupportedOperationException();
	}

	protected void defineType(CAstEntity type, WalkContext wc) {
		throw new UnsupportedOperationException();
	}

	protected void defineField(CAstEntity topEntity, WalkContext wc,
			CAstEntity n) {
		throw new UnsupportedOperationException();
	}

	protected String composeEntityName(WalkContext parent, CAstEntity f) {
		if (f.getKind() == CAstEntity.SCRIPT_ENTITY)
			return f.getName();
		else
//			return parent.getName() + "/" + f.getName();
		throw new UnsupportedOperationException();
	}

	protected void declareFunction(CAstEntity N, WalkContext context) {
//		String fnName = composeEntityName(context, N);
//		if (N.getKind() == CAstEntity.SCRIPT_ENTITY) {
//			((RubyClassLoader) loader).defineScriptType("L" + fnName, N
//					.getPosition());
//		} else if (N.getKind() == CAstEntity.FUNCTION_ENTITY) {
//			((RubyClassLoader) loader).defineFunctionType("L" + fnName, N
//					.getPosition());
//		} else {
//			Assertions.UNREACHABLE();
//		}
		if (N.getKind() == CAstEntity.SCRIPT_ENTITY) {
			//TODO
			System.out.println("RubyCAst2IRTranslator.declareFunction() for script entity");
		} else
			throw new UnsupportedOperationException();
	}

	protected void defineFunction(CAstEntity N, WalkContext definingContext,
			AbstractCFG cfg, SymbolTable symtab, boolean hasCatchBlock,
			TypeReference[][] caughtTypes, LexicalInformation LI,
			DebuggingInformation debugInfo) {
//		if (DEBUG)
//			Trace.println("\n\nAdding code for " + N);
//		String fnName = composeEntityName(definingContext, N);
//
//		if (DEBUG)
//			Trace.println(cfg);
//
//		((RubyClassLoader) loader).defineCodeBodyCode("L" + fnName, cfg,
//				symtab, hasCatchBlock, caughtTypes, LI, debugInfo);
		throw new UnsupportedOperationException();
	}

	protected void doThrow(WalkContext context, int exception) {
//		context.cfg().addInstruction(
//				new NonExceptingThrowInstruction(exception));
		throw new UnsupportedOperationException();
	}

	protected void doCall(WalkContext context, CAstNode call, int result,
			int exception, CAstNode name, int receiver, int[] arguments) {
//		MethodReference ref = name.getValue().equals("ctor") ? RubyMethods.ctorReference
//				: AstMethodReference.fnReference(RubyTypes.CodeBody);
//
//		context.cfg().addInstruction(
//				new RubyInvoke(receiver, result, arguments, exception,
//						new RubyCallSiteReference(ref, context.cfg()
//								.getCurrentInstruction())));
//
//		context.cfg().addPreNode(call, context.getUnwindState());
//
//		context.cfg().newBlock(true);
//
//		if (context.getControlFlow().getTarget(call, null) != null)
//			context.cfg().addPreEdge(call,
//					context.getControlFlow().getTarget(call, null), true);
//		else
//			context.cfg().addPreEdgeToExit(call, true);
		throw new UnsupportedOperationException();
	}

	protected void doNewObject(WalkContext context, CAstNode newNode,
			int result, Object type, int[] arguments) {
//		Assertions._assert(arguments == null);
//		TypeReference typeRef = TypeReference.findOrCreate(
//				RubyTypes.rubyLoader, TypeName.string2TypeName("L" + type));
//
//		context.cfg().addInstruction(
//				new RubyNewInstruction(result, NewSiteReference.make(context
//						.cfg().getCurrentInstruction(), typeRef)));
		throw new UnsupportedOperationException();
	}

	protected void doMaterializeFunction(WalkContext context, int result,
			int exception, CAstEntity fn) {
		throw new UnsupportedOperationException();
	}

	public void doArrayRead(WalkContext context, int result, int arrayValue,
			CAstNode arrayRef, int[] dimValues) {
		throw new UnsupportedOperationException();
	}

	public void doArrayWrite(WalkContext context, int arrayValue,
			CAstNode arrayRef, int[] dimValues, int rval) {
throw new UnsupportedOperationException();
	}

	protected void doFieldRead(WalkContext context, int result, int receiver,
			CAstNode elt, CAstNode parent) {
	throw new UnsupportedOperationException();
	}

	protected void doFieldWrite(WalkContext context, int receiver,
			CAstNode elt, CAstNode parent, int rval) {
		throw new UnsupportedOperationException();
	}

	protected void doPrimitive(int resultVal, WalkContext context,
			CAstNode primitiveCall) {
		throw new UnsupportedOperationException();
	}

	protected void doIsFieldDefined(WalkContext context, int result, int ref,
			CAstNode f) {
		throw new UnsupportedOperationException();
	}

	protected void doPrologue(WalkContext context) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected boolean visitScriptEntity(CAstEntity n, Context context,
			Context codeContext, CAstVisitor visitor) {
		//TODO
		System.out.println("RubyCAst2IRTranslator.visitScriptEntity() " + n);
		return true;
	}

	protected boolean doVisit(CAstNode n, Context cntxt, CAstVisitor visitor) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void translate(CAstEntity N, String nm) {
		super.translate(N, nm);
	}

}
