package com.yoursway.ruby.wala;
import java.util.Collection;
import java.util.Collections;

import com.ibm.wala.ssa.SSAGetInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.FieldReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.Atom;

public class RubyStaticPropertyRead extends SSAGetInstruction {

  public RubyStaticPropertyRead(int result,
				      int objectRef,
				      FieldReference memberRef) {
    super(result, objectRef, memberRef);
  }

  public RubyStaticPropertyRead(int result,
				      int objectRef,
				      String fieldName) 
  {
    this(result, 
	 objectRef, 
	 FieldReference.findOrCreate(
	   RubyTypes.Root,
	   Atom.findOrCreateUnicodeAtom(fieldName),
	   RubyTypes.Root));
  }

  public SSAInstruction copyForSSA(int[] defs, int[] uses) {
    return
      new RubyStaticPropertyRead(
        defs==null? getDef(): defs[0],
	uses==null? getRef(): uses[0],
	getDeclaredField());
  }

  /* (non-Javadoc)
   * @see com.ibm.domo.ssa.Instruction#getExceptionTypes()
   */
  public Collection<TypeReference> getExceptionTypes() {
    return Collections.emptyList();
  }

}
