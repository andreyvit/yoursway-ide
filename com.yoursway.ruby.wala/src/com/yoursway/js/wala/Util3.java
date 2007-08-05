package com.yoursway.js.wala;

import java.util.Collection;

import com.ibm.wala.cast.types.AstMethodReference;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeName;
import com.ibm.wala.types.TypeReference;

public class Util3  {

    public static Collection<CGNode> getNodes(CallGraph CG, String funName) {
      boolean ctor = funName.startsWith("ctor:");
      TypeReference TR = TypeReference.findOrCreate(JavaScriptTypes.jsLoader, TypeName.string2TypeName("L"
          + (ctor ? funName.substring(5) : funName)));
      MethodReference MR = ctor ? JavaScriptMethods.makeCtorReference(TR) : AstMethodReference.fnReference(TR);
      return CG.getNodes(MR);
    }
}
