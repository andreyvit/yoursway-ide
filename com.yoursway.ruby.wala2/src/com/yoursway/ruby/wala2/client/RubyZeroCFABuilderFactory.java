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

import com.ibm.wala.client.CallGraphBuilderFactory;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CallGraphBuilder;
import com.ibm.wala.ipa.callgraph.impl.ClassHierarchyClassTargetSelector;
import com.ibm.wala.ipa.callgraph.impl.ClassHierarchyMethodTargetSelector;
import com.ibm.wala.ipa.callgraph.propagation.cfa.ZeroXInstanceKeys;
import com.ibm.wala.ipa.cha.IClassHierarchy;

public class RubyZeroCFABuilderFactory implements CallGraphBuilderFactory {

	public CallGraphBuilder make(AnalysisOptions options, AnalysisCache cache,
			IClassHierarchy cha, AnalysisScope scope, boolean keepPointsTo) {

		if (options == null) {
			throw new IllegalArgumentException("options is null");
		}
		// addDefaultSelectors(options, cha);
		// addDefaultBypassLogic(options, scope, Util.class.getClassLoader(),
		// cha);

		options.setSelector(new ClassHierarchyMethodTargetSelector(cha));
		options.setSelector(new ClassHierarchyClassTargetSelector(cha));

		return new RubyZeroXCFABuilder(cha, options, cache, null, null, options
				.getReflectionSpec(), ZeroXInstanceKeys.NONE);
	}
}
