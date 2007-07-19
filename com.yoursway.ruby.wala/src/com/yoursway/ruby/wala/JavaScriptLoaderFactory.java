package com.yoursway.ruby.wala;

import com.ibm.wala.cast.loader.SingleClassLoaderFactory;
import com.ibm.wala.classLoader.IClassLoader;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.ClassLoaderReference;

public class JavaScriptLoaderFactory extends SingleClassLoaderFactory {
  private final JavaScriptTranslatorFactory translatorFactory;

  public JavaScriptLoaderFactory(JavaScriptTranslatorFactory factory) {
    this.translatorFactory = factory;
  }

  protected IClassLoader makeTheLoader(IClassHierarchy cha) {
    return new JavaScriptLoader( cha, translatorFactory );
  }

  public ClassLoaderReference getTheReference() {
    return JavaScriptTypes.jsLoader;
  }
}
