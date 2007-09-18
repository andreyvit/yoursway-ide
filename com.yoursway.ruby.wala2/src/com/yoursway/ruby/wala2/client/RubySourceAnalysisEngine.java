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
package com.yoursway.ruby.wala2.client;

import java.io.IOException;

import com.ibm.wala.cast.ir.ssa.AstIRFactory;
import com.ibm.wala.classLoader.ClassLoaderFactory;
import com.ibm.wala.classLoader.SourceFileModule;
import com.ibm.wala.client.impl.AbstractAnalysisEngine;
import com.ibm.wala.eclipse.util.EclipseProjectPath;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ssa.SSAOptions;
import com.ibm.wala.ssa.SymbolTable;
import com.ibm.wala.ssa.Value;
import com.ibm.wala.util.debug.Assertions;
import com.yoursway.ruby.wala2.RubyLanguage;
import com.yoursway.ruby.wala2.ipa.callgraph.RubySourceAnalysisScope;
import com.yoursway.ruby.wala2.loader.RubyLoaderFactory;

public class RubySourceAnalysisEngine extends AbstractAnalysisEngine {

	private RubyLoaderFactory rubyLoaderFactory;

	public RubySourceAnalysisEngine() {
		super();
		rubyLoaderFactory = new RubyLoaderFactory();
		setCallGraphBuilderFactory(new RubyZeroCFABuilderFactory());
	}

	@SuppressWarnings("unchecked")
	protected void buildAnalysisScope() {
		SourceFileModule[] files = (SourceFileModule[]) moduleFiles
				.toArray(new SourceFileModule[moduleFiles.size()]);
		try {
			scope = new RubySourceAnalysisScope(files, rubyLoaderFactory);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}

	protected ClassLoaderFactory getClassLoaderFactory() {
		return rubyLoaderFactory;
	}

	protected IClassHierarchy buildClassHierarchy() {
		IClassHierarchy cha = null;
		ClassLoaderFactory factory = getClassLoaderFactory();

		try {
			cha = ClassHierarchy.make(getScope(), factory,
					RubyLanguage.LANGUAGE);
		} catch (ClassHierarchyException e) {
			System.err.println("Class Hierarchy construction failed");
			System.err.println(e.toString());
			e.printStackTrace();
		}
		return cha;
	}

	protected Iterable<Entrypoint> makeDefaultEntrypoints(AnalysisScope scope,
			IClassHierarchy cha) {
		return Util.makeMainEntrypoints(EclipseProjectPath.SOURCE_REF, cha);
	}

	@Override
	public AnalysisCache makeDefaultCache() {
		return new AnalysisCache(AstIRFactory.makeDefaultFactory(true));
	}

	public AnalysisOptions getDefaultOptions(Iterable<Entrypoint> entrypoints) {
		AnalysisOptions options = new AnalysisOptions(getScope(), entrypoints);

		SSAOptions ssaOptions = new SSAOptions();
		ssaOptions.setDefaultValues(new SSAOptions.DefaultValues() {
			public int getDefaultValue(SymbolTable symtab, int valueNumber) {
				Value v = symtab.getValue(valueNumber);
				if (v == null) {
					Assertions._assert(v != null, "no default for "
							+ valueNumber);
				}
				return v.getDefaultValue(symtab);
			}
		});

		options.setSSAOptions(ssaOptions);

		return options;
	}
}
