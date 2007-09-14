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
//
//	/**
//	 * Modules which are user-space code
//	 */
//	private final Set<Module> userEntries = HashSetFactory.make();
//
//	/**
//	 * Modules which are source code
//	 */
//	private final Set<Module> sourceEntries = HashSetFactory.make();
//
//	/**
//	 * Modules which are system or library code TODO: what about extension
//	 * loader?
//	 */
//	private final Set<Module> systemEntries = HashSetFactory.make();

	private RubyLoaderFactory rubyLoaderFactory;

	public RubySourceAnalysisEngine() {
		super();
		rubyLoaderFactory = new RubyLoaderFactory();
		setCallGraphBuilderFactory(new RubyZeroCFABuilderFactory());
	}

//	/**
//	 * Adds the given source module to the source loader's module list. Clients
//	 * should/may call this method if they don't supply an IJavaProject to the
//	 * constructor.
//	 */
//	public void addSourceModule(Module M) {
//		sourceEntries.add(M);
//	}
//
//	/**
//	 * Adds the given compiled module to the application loader's module list.
//	 * Clients should/may call this method if they don't supply an IJavaProject
//	 * to the constructor.
//	 */
//	public void addCompiledModule(Module M) {
//		userEntries.add(M);
//	}
//
//	/**
//	 * Adds the given module to the primordial loader's module list. Clients
//	 * should/may call this method if they don't supply an IJavaProject to the
//	 * constructor.
//	 */
//	public void addSystemModule(Module M) {
//		systemEntries.add(M);
//	}

//	protected void addApplicationModulesToScope() {
//		ClassLoaderReference src = ((RubySourceAnalysisScope) scope)
//				.getLoader();
//
//		for (Module M : sourceEntries) {
//			scope.addToScope(src, M);
//		}
//	}

	protected void buildAnalysisScope() {
		SourceFileModule[] files = (SourceFileModule[]) moduleFiles
				.toArray(new SourceFileModule[moduleFiles.size()]);
		try {
			scope = new RubySourceAnalysisScope(files, rubyLoaderFactory);
		} catch (IOException e) {
			throw new AssertionError(e);
		}

//		// add user stuff
//		addApplicationModulesToScope();
	}
//
//	public IRTranslatorExtension getTranslatorExtension() {
//		return new JavaIRTranslatorExtension();
//	}

	protected ClassLoaderFactory getClassLoaderFactory() {
		return rubyLoaderFactory;
	}

	protected IClassHierarchy buildClassHierarchy() {
		IClassHierarchy cha = null;
		ClassLoaderFactory factory = getClassLoaderFactory();

		try {
			cha = ClassHierarchy.make(getScope(), factory, RubyLanguage.LANGUAGE);
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
