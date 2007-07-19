package com.yoursway.ruby.wala;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import com.ibm.wala.cast.ir.translator.TranslatorToIR;
import com.ibm.wala.classLoader.ModuleEntry;
import com.ibm.wala.classLoader.SourceFileModule;
import com.ibm.wala.classLoader.SourceURLModule;
import com.ibm.wala.util.collections.HashSetFactory;
import com.yoursway.ruby.wala.internal.Activator;

public abstract class TranslatorBase implements TranslatorToIR {

  protected static final Set<String> bootstrapFileNames;

  private static String prologueFileName = "prologue.js";

  public static void resetPrologueFile() {
    prologueFileName = "prologue.js";
  }

  public static void setPrologueFile(String name) {
    prologueFileName = name;
  }

  public static void addBootstrapFile(String fileName) {
    bootstrapFileNames.add(fileName);
  }

  static {
    bootstrapFileNames = HashSetFactory.make();
    bootstrapFileNames.add(prologueFileName);
  }

  public abstract void translate(ModuleEntry M, String N) throws IOException;

  public void translate(Set modules) throws IOException {
    translate(new SourceURLModule(Activator.getDefault().getBundle().getEntry("data/miniprologue.js")), "miniprologue.js");
    Iterator MS = modules.iterator();
    while (MS.hasNext()) {
      ModuleEntry M = (ModuleEntry) MS.next();
      if (M instanceof SourceFileModule) {
        translate(M, ((SourceFileModule) M).getClassName());
      } else {
        translate(M, M.getName());
      }
    }
  }
}
