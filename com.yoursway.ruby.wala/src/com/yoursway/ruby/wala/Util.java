package com.yoursway.ruby.wala;

import java.util.Collection;
import java.util.Collections;

import com.ibm.wala.types.TypeReference;

class Util {
  private static final Collection<TypeReference> TYPE_ERROR_EXCEPTIONS = Collections.unmodifiableCollection(Collections
      .singleton(JavaScriptTypes.TypeError));

  public static Collection<TypeReference> typeErrorExceptions() {
    return TYPE_ERROR_EXCEPTIONS;
  }

  public static Collection<TypeReference> noExceptions() {
    return Collections.emptySet();
  }

}
