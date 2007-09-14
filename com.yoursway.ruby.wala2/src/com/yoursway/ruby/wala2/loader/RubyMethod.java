package com.yoursway.ruby.wala2.loader;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.types.Descriptor;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.Selector;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.Atom;

public class RubyMethod implements IMethod {

	
	
	public RubyMethod() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TypeReference[] getDeclaredExceptions()
			throws InvalidClassFileException {
		// TODO Auto-generated method stub
		return null;
	}

	public Descriptor getDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getLineNumber(int bcIndex) throws InvalidClassFileException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getLocalVariableName(int bcIndex, int localNumber)
			throws InvalidClassFileException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMaxLocals() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxStackHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNumberOfParameters() {
		// TODO Auto-generated method stub
		return 0;
	}

	public TypeReference getParameterType(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public MethodReference getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	public TypeReference getReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

	public Selector getSelector() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSignature() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasExceptionHandler() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasLocalVariableTable() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isAbstract() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isClinit() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isFinal() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isInit() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isNative() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPrivate() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isProtected() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPublic() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSynchronized() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSynthetic() {
		// TODO Auto-generated method stub
		return false;
	}

	public IClass getDeclaringClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public Atom getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isStatic() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isVolatile() {
		// TODO Auto-generated method stub
		return false;
	}

	public IClassHierarchy getClassHierarchy() {
		// TODO Auto-generated method stub
		return null;
	}

}
