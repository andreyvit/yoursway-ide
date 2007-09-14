/******************************************************************************
 * Copyright (c) 2002 - 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *****************************************************************************/
/*
 * Created on Sep 27, 2005
 */
package com.yoursway.ruby.wala2.ipa.callgraph;

import java.io.IOException;

import com.ibm.wala.cast.ipa.callgraph.CAstAnalysisScope;
import com.ibm.wala.cast.loader.SingleClassLoaderFactory;
import com.ibm.wala.classLoader.SourceFileModule;
import com.ibm.wala.types.ClassLoaderReference;

public class RubySourceAnalysisScope extends CAstAnalysisScope {

	public RubySourceAnalysisScope(String[] sourceFileNames,
			SingleClassLoaderFactory loaders) throws IOException {
		super(sourceFileNames, loaders);
	}

	public RubySourceAnalysisScope(SourceFileModule[] sources,
			SingleClassLoaderFactory loaders) throws IOException {
		super(sources, loaders);
	}

	public ClassLoaderReference getLoader() {
		return this.getLoaders().iterator().next();
	}

}
