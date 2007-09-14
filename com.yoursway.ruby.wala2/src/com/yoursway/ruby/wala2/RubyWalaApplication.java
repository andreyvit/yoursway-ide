package com.yoursway.ruby.wala2;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collections;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import com.ibm.wala.classLoader.SourceFileModule;
import com.ibm.wala.util.io.Streams;
import com.yoursway.ruby.wala2.client.RubySourceAnalysisEngine;

public class RubyWalaApplication implements IApplication {

	public Object start(IApplicationContext context) throws Exception {
		File f = new File("/tmp/test.rb");
		PrintWriter writer = new PrintWriter(new FileWriter(f));
		URL entry = Activator.getDefault().getBundle().getEntry(
				"data/rabbit.rb");
		InputStream is = entry.openStream();
		byte[] ba = Streams.inputStream2ByteArray(is);
		is.close();
		String text = new String(ba);
		writer.println(text);
		writer.close();

		SourceFileModule sfm = new SourceFileModule(f, "tests/rabbit.rb");

		RubySourceAnalysisEngine engine = new RubySourceAnalysisEngine();

		engine.setModuleFiles(Collections.singleton(sfm));
		engine.buildDefaultCallGraph();
		return null;
	}

	public void stop() {
	}

}
