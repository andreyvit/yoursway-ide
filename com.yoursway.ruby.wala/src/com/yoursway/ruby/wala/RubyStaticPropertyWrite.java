package com.yoursway.ruby.wala;

import java.util.Collection;
import java.util.Collections;

import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAPutInstruction;
import com.ibm.wala.types.FieldReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.Atom;

public class RubyStaticPropertyWrite extends SSAPutInstruction {
    
    public RubyStaticPropertyWrite(int objectRef, FieldReference memberRef, int value) {
        super(objectRef, value, memberRef);
    }
    
    public RubyStaticPropertyWrite(int objectRef, String fieldName, int value) {
        this(objectRef, FieldReference.findOrCreate(RubyTypes.Root, Atom.findOrCreateUnicodeAtom(fieldName),
                RubyTypes.Root), value);
    }
    
    public SSAInstruction copyForSSA(int[] defs, int[] uses) {
        return new RubyStaticPropertyWrite(uses == null ? getRef() : uses[0], getDeclaredField(),
                uses == null ? getVal() : uses[1]);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.domo.ssa.Instruction#getExceptionTypes()
     */
    public Collection<TypeReference> getExceptionTypes() {
        return Collections.emptyList();
    }
    
}
