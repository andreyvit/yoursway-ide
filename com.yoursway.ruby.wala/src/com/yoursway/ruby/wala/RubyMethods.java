package com.yoursway.ruby.wala;

import com.ibm.wala.cast.types.AstMethodReference;
import com.ibm.wala.types.Descriptor;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.Atom;

public class RubyMethods extends AstMethodReference {

  public final static String ctorAtomStr = "ctor";
  public final static Atom ctorAtom = Atom.findOrCreateUnicodeAtom(ctorAtomStr);
  public final static String ctorDescStr = "()LRoot;";
  public final static Descriptor ctorDesc = Descriptor.findOrCreateUTF8(ctorDescStr);
  public final static MethodReference ctorReference =
    MethodReference.findOrCreate(RubyTypes.CodeBody, ctorAtom, ctorDesc);

  public static MethodReference makeCtorReference(TypeReference cls) {
    return MethodReference.findOrCreate(cls, ctorAtom, ctorDesc);
  }

}
