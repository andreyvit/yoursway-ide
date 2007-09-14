package com.yoursway.ruby.wala2.loader;

import java.util.Collection;
import java.util.Collections;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IClassLoader;
import com.ibm.wala.classLoader.IField;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.Selector;
import com.ibm.wala.types.TypeName;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.Atom;
import com.yoursway.ruby.wala2.RubyLanguage;
import com.yoursway.ruby.wala2.ipa.callgraph.RubyFakeWorldClinitMethod;

public class RubyClass implements IClass { 

	private String className;
	private final RubyClassLoader loader;
	
	public RubyClass(RubyClassLoader loader, String className) {
		super();
		this.loader = loader;
		this.className = className;
	}

	public Collection<IClass> getAllAncestorInterfaces()
			throws ClassHierarchyException {
		throw new UnsupportedOperationException();
	}

	public Collection<IField> getAllFields() throws ClassHierarchyException {
		throw new UnsupportedOperationException();
	}

	public Collection<IClass> getAllImplementedInterfaces()
			throws ClassHierarchyException {
		return Collections.emptyList();
//		throw new UnsupportedOperationException();
	}

	public Collection<IField> getAllInstanceFields()
			throws ClassHierarchyException {
		throw new UnsupportedOperationException();
	}

	public Collection<IMethod> getAllMethods() throws ClassHierarchyException {
		throw new UnsupportedOperationException();
	}

	public Collection<IField> getAllStaticFields()
			throws ClassHierarchyException {
		throw new UnsupportedOperationException();
	}

	public IMethod getClassInitializer() {
		throw new UnsupportedOperationException();
	}

	public IClassLoader getClassLoader() {
		return loader;
	}

	public Collection<IField> getDeclaredInstanceFields() {
		throw new UnsupportedOperationException();
	}

	public Collection<IMethod> getDeclaredMethods() {
		throw new UnsupportedOperationException();
	}

	public Collection<IField> getDeclaredStaticFields() {
		throw new UnsupportedOperationException();
	}

	public Collection<IClass> getDirectInterfaces()
			throws ClassHierarchyException {
		throw new UnsupportedOperationException();
	}

	public IField getField(Atom name) {
		throw new UnsupportedOperationException();
	}

	public IMethod getMethod(Selector selector) {
		if (selector.getName().toString().equals("fakeWorldClinit")) {
			return new RubyFakeWorldClinitMethod(null, null, null);
		}
		throw new UnsupportedOperationException();
	}

	public int getModifiers() {
		throw new UnsupportedOperationException();
	}

	public TypeName getName() {
		return TypeName.findOrCreate(className);
	}

	public TypeReference getReference() {
		return TypeReference.findOrCreate(RubyLanguage.LOADER_REF, className);
	}

	public String getSourceFileName() {
		throw new UnsupportedOperationException();
	}

	public IClass getSuperclass() throws ClassHierarchyException {
		System.out.println("RubyClass.getSuperclass()");
		return null;
//		throw new UnsupportedOperationException();
	}

	public boolean isAbstract() {
		throw new UnsupportedOperationException();
	}

	public boolean isArrayClass() {
		return (this.getName().getClassName().toString().equals("Array"));
		//throw new UnsupportedOperationException();
	}

	public boolean isInterface() {
		return false;
	}

	public boolean isPublic() {
		throw new UnsupportedOperationException();
	}

	public boolean isReferenceType() {
		throw new UnsupportedOperationException();
	}

	public IClassHierarchy getClassHierarchy() {
		// TODO Auto-generated method stub
		return null;
	}

}
