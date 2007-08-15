package com.yoursway.ruby.wala;
import java.util.Collection;
import java.util.Collections;

import com.ibm.wala.classLoader.NewSiteReference;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSANewInstruction;
import com.ibm.wala.types.TypeReference;

/**
 * Ruby instance creation IR instruction. Returns Ruby-specific exceptions.
 * 
 * @author Andrey Tarantsov
 */
public class RubyNewInstruction extends SSANewInstruction {
  
  public RubyNewInstruction(int result, NewSiteReference site) {
    super(result, site);
  }

  public SSAInstruction copyForSSA(int[] defs, int[] uses) {
    return
      new RubyNewInstruction(
        defs==null? getDef(): defs[0],
	getNewSite());
  }

  public Collection<TypeReference> getExceptionTypes() {
    return Collections.emptyList();
  }

}
