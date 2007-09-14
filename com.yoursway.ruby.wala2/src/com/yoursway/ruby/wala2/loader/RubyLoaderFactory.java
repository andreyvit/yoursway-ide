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
package com.yoursway.ruby.wala2.loader;

import com.ibm.wala.cast.loader.SingleClassLoaderFactory;
import com.ibm.wala.classLoader.IClassLoader;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.ClassLoaderReference;
import com.yoursway.ruby.wala2.RubyLanguage;

public class RubyLoaderFactory extends SingleClassLoaderFactory {

	public RubyLoaderFactory() {
	}

	@Override
	public ClassLoaderReference getTheReference() {
		return RubyLanguage.LOADER_REF;
	}

	@Override
	protected IClassLoader makeTheLoader(IClassHierarchy cha) {
		return new RubyClassLoader();
	}

}
