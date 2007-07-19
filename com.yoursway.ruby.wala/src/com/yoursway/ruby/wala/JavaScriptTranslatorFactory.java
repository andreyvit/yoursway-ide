package com.yoursway.ruby.wala;

import com.ibm.wala.cast.ir.translator.TranslatorToIR;

public interface JavaScriptTranslatorFactory {

  TranslatorToIR make(JavaScriptLoader loader);

  public static class CAstRhinoFactory implements JavaScriptTranslatorFactory {

    public TranslatorToIR make(JavaScriptLoader loader) {
       return new CAstRhinoTranslator(loader);
    }
  }

}
