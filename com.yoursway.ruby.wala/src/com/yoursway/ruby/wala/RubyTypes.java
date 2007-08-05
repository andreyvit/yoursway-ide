package com.yoursway.ruby.wala;

import com.ibm.wala.cast.types.AstTypeReference;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.Atom;

public class RubyTypes extends AstTypeReference {
    
    public static final String rubyNameStr = "Ruby";
    
    public static final String rubyLoaderNameStr = "RubyLoader";
    
    public static final Atom rubyName = Atom.findOrCreateUnicodeAtom(rubyNameStr);
    
    public static final Atom rubyLoaderName = Atom.findOrCreateUnicodeAtom(rubyLoaderNameStr);
    
    public static final ClassLoaderReference rubyLoader = new ClassLoaderReference(rubyLoaderName, rubyName);
    
    public static final TypeReference Root = TypeReference.findOrCreate(rubyLoader, rootTypeName);
    
    public static final TypeReference Undefined = TypeReference.findOrCreate(rubyLoader, "LUndefined");
    
    public static final TypeReference Null = TypeReference.findOrCreate(rubyLoader, "LNull");
    
    public static final TypeReference Array = TypeReference.findOrCreate(rubyLoader, "LArray");
    
    public static final TypeReference Object = TypeReference.findOrCreate(rubyLoader, "LObject");
    
    public static final TypeReference CodeBody = TypeReference.findOrCreate(rubyLoader, functionTypeName);
    
    public static final TypeReference Function = TypeReference.findOrCreate(rubyLoader, "LFunction");
    
    public static final TypeReference Script = TypeReference.findOrCreate(rubyLoader, "LScript");
    
    public static final TypeReference TypeError = TypeReference.findOrCreate(rubyLoader, "LTypeError");
    
    public static final TypeReference Primitives = TypeReference.findOrCreate(rubyLoader, "LPrimitives");
    
    public static final TypeReference FakeRoot = TypeReference.findOrCreate(rubyLoader, "LFakeRoot");
    
    public static final TypeReference Boolean = TypeReference.findOrCreate(rubyLoader, "LBoolean");
    
    public static final TypeReference String = TypeReference.findOrCreate(rubyLoader, "LString");
    
    public static final TypeReference Number = TypeReference.findOrCreate(rubyLoader, "LNumber");
    
    public static final TypeReference Date = TypeReference.findOrCreate(rubyLoader, "LDate");
    
    public static final TypeReference RegExp = TypeReference.findOrCreate(rubyLoader, "LRegExp");
    
    public static final TypeReference BooleanObject = TypeReference.findOrCreate(rubyLoader, "LBooleanObject");
    
    public static final TypeReference StringObject = TypeReference.findOrCreate(rubyLoader, "LStringObject");
    
    public static final TypeReference NumberObject = TypeReference.findOrCreate(rubyLoader, "LNumberObject");
    
    public static final TypeReference DateObject = TypeReference.findOrCreate(rubyLoader, "LDateObject");
    
    public static final TypeReference RegExpObject = TypeReference.findOrCreate(rubyLoader, "LRegExpObject");
    
}
