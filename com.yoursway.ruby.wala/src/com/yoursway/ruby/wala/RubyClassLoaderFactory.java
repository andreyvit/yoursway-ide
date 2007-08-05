package com.yoursway.ruby.wala;

import com.ibm.wala.cast.loader.SingleClassLoaderFactory;
import com.ibm.wala.classLoader.IClassLoader;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.ClassLoaderReference;
import com.yoursway.ruby.wala.classloader.RubyClassLoader;

public class RubyClassLoaderFactory extends SingleClassLoaderFactory {

  public RubyClassLoaderFactory() {
  }

  protected IClassLoader makeTheLoader(IClassHierarchy cha) {
    return new RubyClassLoader( cha );
  }

  public ClassLoaderReference getTheReference() {
    return RubyTypes.rubyLoader;
  }
}
