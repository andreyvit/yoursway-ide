package com.yoursway.ruby.wala2;

import com.ibm.wala.classLoader.Language;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.Atom;

public class RubyLanguage implements Language {
	public final static Atom rubyLanguageName = Atom.findOrCreateUnicodeAtom("Ruby");
	public final static ClassLoaderReference LOADER_REF = new ClassLoaderReference(Atom.findOrCreateUnicodeAtom("RubyClassLoader"), rubyLanguageName);
	
	public final static RubyLanguage LANGUAGE = new RubyLanguage();


	private RubyLanguage() {}
	
	public TypeReference getConstantType(Object o) {
		throw new UnsupportedOperationException();
	}

	public Atom getName() {
		return rubyLanguageName;
	}

	public TypeReference getRootType() {
		return TypeReference.findOrCreate(LOADER_REF, "Object");
//		throw new UnsupportedOperationException();
	}

	public boolean isNullType(TypeReference type) {
		throw new UnsupportedOperationException();
	}

}
