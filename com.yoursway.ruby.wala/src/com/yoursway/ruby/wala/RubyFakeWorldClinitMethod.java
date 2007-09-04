package com.yoursway.ruby.wala;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.impl.AbstractRootMethod;
import com.ibm.wala.ipa.callgraph.impl.FakeRootClass;

import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.Descriptor;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.util.Atom;

/**
 * 
 * A synthetic method that calls all class initializers
 * 
 * @author sfink
 */
public class RubyFakeWorldClinitMethod extends AbstractRootMethod {

  private static final Atom name = Atom.findOrCreateAsciiAtom("fakeWorldClinit");

  private static final MethodReference worldClinitMethod = MethodReference.findOrCreate(FakeRootClass.FAKE_ROOT_CLASS, name, Descriptor
      .findOrCreateUTF8("()V"));
  
  public RubyFakeWorldClinitMethod(final IClassHierarchy cha, AnalysisOptions options, AnalysisCache cache) {
    super(worldClinitMethod, cha, options, cache);
  }
}
