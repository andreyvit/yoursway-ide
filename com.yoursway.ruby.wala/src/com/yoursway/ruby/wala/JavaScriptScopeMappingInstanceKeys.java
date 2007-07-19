package com.yoursway.ruby.wala;

import com.ibm.wala.cast.ipa.callgraph.ScopeMappingInstanceKeys;
import com.ibm.wala.cast.loader.AstMethod.LexicalParent;
import com.ibm.wala.cast.types.AstMethodReference;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKeyFactory;
import com.ibm.wala.ipa.callgraph.propagation.PropagationCallGraphBuilder;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.yoursway.ruby.wala.JavaScriptLoader.JavaScriptMethodObject;

public class JavaScriptScopeMappingInstanceKeys extends ScopeMappingInstanceKeys {

  private final IClassHierarchy cha;
  private final IClass codeBody;

  public JavaScriptScopeMappingInstanceKeys(IClassHierarchy cha,
					    PropagationCallGraphBuilder builder, 
					    InstanceKeyFactory basic)
  {
    super(builder, basic);
    this.cha = cha;

    this.codeBody = cha.lookupClass(JavaScriptTypes.CodeBody);
  }

  protected LexicalParent[] getParents(InstanceKey base) {
    JavaScriptMethodObject function = (JavaScriptMethodObject)
      base.getConcreteType().getMethod(AstMethodReference.fnSelector);

    return function==null? new LexicalParent[0]: function.getParents();
  }

  protected boolean needsScopeMappingKey(InstanceKey base) {
    return 
      cha.isSubclassOf(base.getConcreteType(), codeBody)
                        &&
      getParents(base).length > 0;
  }

}
