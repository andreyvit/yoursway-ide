package com.yoursway.ruby.wala;

import java.io.IOException;

import com.ibm.wala.cast.tree.impl.CAstImpl;
import com.ibm.wala.classLoader.ModuleEntry;

public class CAstRhinoTranslator extends TranslatorBase {
    private final JSAstTranslator CAstToIR;

    public void translate(ModuleEntry M, String N) throws IOException {
      CAstImpl Ast = new CAstImpl();
      CAstToIR.translate(
	new PropertyReadExpander(Ast).rewrite(
          new RhinoToAstTranslator(Ast, M, N).translate()),
	N);
    }

    public CAstRhinoTranslator(JavaScriptLoader loader) {
      this.CAstToIR = new JSAstTranslator(loader);
    }

}
