/**
 * 
 */
package com.yoursway.ruby.wala.classloader;

import com.ibm.wala.classLoader.Language;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.Atom;
import com.yoursway.ruby.wala.RubyTypes;

final class RubyLanguage implements Language {
    public Atom getName() {
        return Atom.findOrCreateUnicodeAtom("Ruby");
    }
    
    public TypeReference getRootType() {
        return RubyTypes.Root;
    }
    
    public TypeReference getConstantType(Object o) {
        if (o == null) {
            return RubyTypes.Null;
        } else {
            Class<?> c = o.getClass();
            if (c == Boolean.class) {
                return RubyTypes.Boolean;
            } else if (c == String.class) {
                return RubyTypes.String;
            } else if (c == Integer.class) {
                return RubyTypes.Number;
            } else if (c == Float.class) {
                return RubyTypes.Number;
            } else if (c == Double.class) {
                return RubyTypes.Number;
            } else {
                assert false : "cannot determine type for " + o + " of class " + c;
                return null;
            }
        }
    }
    
    public boolean isNullType(TypeReference type) {
        return type.equals(RubyTypes.Undefined) || type.equals(RubyTypes.Null);
    }
}