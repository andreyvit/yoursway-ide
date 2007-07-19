package com.yoursway.ruby.wala;
import java.util.Collection;

import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAPutInstruction;
import com.ibm.wala.types.FieldReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.Atom;

public class JavaScriptStaticPropertyWrite extends SSAPutInstruction {
  
  public JavaScriptStaticPropertyWrite(int objectRef,
				       FieldReference memberRef,
				       int value) {
    super(objectRef, value, memberRef);
  }

  public JavaScriptStaticPropertyWrite(int objectRef,
				       String fieldName,
				       int value)
  {
    this(objectRef, 
	 FieldReference.findOrCreate(
	   JavaScriptTypes.Root,
	   Atom.findOrCreateUnicodeAtom(fieldName),
	   JavaScriptTypes.Root),
	 value);
  }

  public SSAInstruction copyForSSA(int[] defs, int[] uses) {
    return
      new JavaScriptStaticPropertyWrite(
        uses==null? getRef(): uses[0],
	getDeclaredField(),
	uses==null? getVal(): uses[1]);
  }

  /* (non-Javadoc)
   * @see com.ibm.domo.ssa.Instruction#getExceptionTypes()
   */
  public Collection<TypeReference> getExceptionTypes() {
    return Util.typeErrorExceptions();
  }

}
