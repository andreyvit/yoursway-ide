package com.yoursway.js.wala;

import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.debug.Assertions;


import com.ibm.wala.cast.ir.ssa.*;
import com.ibm.wala.cast.ir.ssa.AstLexicalAccess.Access;
import com.ibm.wala.classLoader.*;
import com.ibm.wala.ssa.*;

import java.util.*;

public class JavaScriptInvoke extends AbstractLexicalInvoke {
  /**
   * The value numbers of the arguments passed to the call.
   */
  private final int[] params;

  private int function;

  public JavaScriptInvoke(int function, int result, int[] params, int exception, CallSiteReference site) {
    super(result, exception, site);
    this.function = function;
    this.params = params;
  }

  private JavaScriptInvoke(int function, int result, int[] params, int exception, CallSiteReference site, Access[] lexicalReads, Access[] lexicalWrites) {
    super(result, exception, site, lexicalReads, lexicalWrites);
    this.function = function;
    this.params = params;
  }

  /**
   * Constructor InvokeInstruction. This case for void return values
   * @param i
   * @param params
   */
  public JavaScriptInvoke(int function, int[] params, int exception, CallSiteReference site) {
    this(function, -1, params, exception, site);
  }

  public SSAInstruction copyForSSA(int[] defs, int[] uses) {
    int fn = function;
    int newParams[] = params;
    Access[] reads = lexicalReads;

    if (uses != null) {
      int i = 0;

      fn = uses[i++];

      newParams = new int[ params.length ];
      for(int j = 0; j < newParams.length; j++)
	newParams[j] = uses[i++];

      if (lexicalReads != null) {
	reads = new Access[ lexicalReads.length ];
	for(int j = 0; j < reads.length; j++)
	  reads[j] = new Access(lexicalReads[j].variableName, lexicalReads[j].variableDefiner, uses[i++]);
      }
    }

    int newLval = result;
    int newExp = exception;
    Access[] writes = lexicalWrites;
    
    if (defs != null) {
      int i = 0;
      newLval = defs[i++];
      newExp = defs[i++];
      
      if (lexicalWrites != null) {
	writes = new Access[ lexicalWrites.length ];
	for(int j = 0; j < writes.length; j++)
	  writes[j] = new Access(lexicalWrites[j].variableName, lexicalWrites[j].variableDefiner, defs[i++]);
      }
    }

    return new JavaScriptInvoke(fn, newLval, newParams, newExp, site, reads, writes);
  }
    
  public String toString(SymbolTable symbolTable, ValueDecorator d) {
    StringBuffer s = new StringBuffer();
    if (result != -1) {
      s.append(getValueString(symbolTable, d, result)).append(" = ");
    }
    if (site.getDeclaredTarget().equals(JavaScriptMethods.ctorReference))
      s.append("construct ");
    else
      s.append("invoke ");
    s.append(getValueString(symbolTable, d, function));

    if (site != null) s.append("@").append(site.getProgramCounter());

    if (params != null) {
      if (params.length > 0) {
        s.append(" ").append(getValueString(symbolTable, d, params[0]));
      }
      for (int i = 1; i < params.length; i++) {
        s.append(",").append(getValueString(symbolTable, d, params[i]));
      }
    }
    
    if (exception == -1) {
      s.append(" exception: NOT MODELED");
    } else {
      s.append(" exception:").append(getValueString(symbolTable, d, exception));
    }
    
    if (lexicalReads != null) {
      s.append(" (reads:");
      for(int i = 0; i < lexicalReads.length; i++) {
	s.append(" ").append(lexicalReads[i].variableName).append(":").append( getValueString(symbolTable, d, lexicalReads[i].valueNumber) );
      }
      s.append(")");
    }

    if (lexicalWrites != null) {
      s.append(" (writes:");
      for(int i = 0; i < lexicalWrites.length; i++) {
	s.append(" ").append(lexicalWrites[i].variableName).append(":").append( getValueString(symbolTable, d, lexicalWrites[i].valueNumber) );
      }
      s.append(")");
    }

    return s.toString();
  }

  /**
   * @see com.ibm.domo.ssa.Instruction#visit(Visitor)
   */
  public void visit(IVisitor v) {
    Assertions._assert(v instanceof XInstructionVisitor);
    ((XInstructionVisitor)v).visitJavaScriptInvoke(this);
  }

  public int getNumberOfParameters() {
    if (params == null) {
      return 1;
    } else {
      return params.length+1;
    }
  }

  /**
   * @see com.ibm.domo.ssa.Instruction#getUse(int)
   */
  public int getUse(int j) {
    if (j == 0)
      return function;
    else if (j <= params.length)
      return params[j-1];
    else {
      return super.getUse(j);
    }
  }

  public int getFunction() {
    return function;
  }

  /* (non-Javadoc)
   * @see com.ibm.domo.ssa.Instruction#getExceptionTypes()
   */
  public Collection<TypeReference> getExceptionTypes() {
    return Util.typeErrorExceptions();
  }

  public int hashCode() {
    return site.hashCode() * function * 7529;
  }

//  public boolean equals(Object obj) {
//    if (obj instanceof JavaScriptInvoke) {
//      JavaScriptInvoke other = (JavaScriptInvoke)obj;
//      if (site.equals(other.site)) {
//	if (getNumberOfUses() == other.getNumberOfUses()) {
//	  for(int i = 0; i < getNumberOfUses(); i++) {
//	    if (getUse(i) != other.getUse(i)) {
//	      return false;
//	    }
//	  }
//
//	  if (getNumberOfDefs() == other.getNumberOfDefs()) {
//	    for(int i = 0; i < getNumberOfDefs(); i++) {
//	      if (getDef(i) != other.getDef(i)) {
//		return false;
//	      }
//	    }
//
//	    return true;
//	  }
//	}
//      }
//    }
//
//    return false;
//  }
}
