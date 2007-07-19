package com.yoursway.ruby.wala;

import java.util.Collection;

import com.ibm.wala.ssa.SSAGetInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.FieldReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.Atom;

public class JavaScriptStaticPropertyRead extends SSAGetInstruction {

  public JavaScriptStaticPropertyRead(int result,
				      int objectRef,
				      FieldReference memberRef) {
    super(result, objectRef, memberRef);
  }

  public JavaScriptStaticPropertyRead(int result,
				      int objectRef,
				      String fieldName) 
  {
    this(result, 
	 objectRef, 
	 FieldReference.findOrCreate(
	   JavaScriptTypes.Root,
	   Atom.findOrCreateUnicodeAtom(fieldName),
	   JavaScriptTypes.Root));
  }

  public SSAInstruction copyForSSA(int[] defs, int[] uses) {
    return
      new JavaScriptStaticPropertyRead(
        defs==null? getDef(): defs[0],
	uses==null? getRef(): uses[0],
	getDeclaredField());
  }

  /* (non-Javadoc)
   * @see com.ibm.domo.ssa.Instruction#getExceptionTypes()
   */
  public Collection<TypeReference> getExceptionTypes() {
    return Util.typeErrorExceptions();
  }

}
