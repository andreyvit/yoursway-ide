package com.yoursway.ruby.wala2;

import com.ibm.wala.types.TypeReference;

public class RubyConstants {

	public final static TypeReference OBJECT_REF =  TypeReference.findOrCreate(
			RubyLanguage.LOADER_REF, "Object");
	
}
