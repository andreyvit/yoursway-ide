package com.yoursway.ruby.wala;
import com.ibm.wala.cast.ipa.callgraph.ScriptEntryPoints;
import com.ibm.wala.classLoader.IClassLoader;
import com.ibm.wala.ipa.cha.IClassHierarchy;

public class RubyEntryPoints extends ScriptEntryPoints {

  public RubyEntryPoints(IClassHierarchy cha, IClassLoader loader) {
    super(cha, loader.lookupClass(RubyTypes.Script.getName()));
  }
    
}
	  
