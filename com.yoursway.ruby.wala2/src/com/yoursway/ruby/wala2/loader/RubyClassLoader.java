package com.yoursway.ruby.wala2.loader;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IClassLoader;
import com.ibm.wala.classLoader.Language;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.TypeName;
import com.ibm.wala.util.Atom;
import com.yoursway.ruby.wala2.RubyLanguage;

public class RubyClassLoader implements IClassLoader {

	private RubyClass objectClass;

	public RubyClassLoader() {
		objectClass = new RubyClass(this, "Object");
	}

	public Language getLanguage() {
		return RubyLanguage.LANGUAGE;
	}

	public Atom getName() {
		throw new UnsupportedOperationException();
	}

	public int getNumberOfClasses() {
		throw new UnsupportedOperationException();
	}

	public int getNumberOfMethods() {
		throw new UnsupportedOperationException();
	}

	public IClassLoader getParent() {
		return null;
	}

	public ClassLoaderReference getReference() {
		return RubyLanguage.LOADER_REF;
	}

	public String getSourceFileName(IClass klass) {
		throw new UnsupportedOperationException();
	}

	public void init(Set modules) throws IOException {
		System.out.println("RubyClassLoader.init() with " + modules.size() + " modules");
//		throw new UnsupportedOperationException();
	}

	public Iterator<IClass> iterateAllClasses() {
		return Collections.singleton((IClass) objectClass).iterator();
		// throw new UnsupportedOperationException();
	}

	public IClass lookupClass(TypeName className) {
		if (className.equals(objectClass.getName())) 
			return objectClass;
		throw new UnsupportedOperationException();
	}

	public void removeAll(Collection<IClass> toRemove) {
		System.out.println("RubyClassLoader.removeAll()");
//		throw new UnsupportedOperationException();
	}

}
