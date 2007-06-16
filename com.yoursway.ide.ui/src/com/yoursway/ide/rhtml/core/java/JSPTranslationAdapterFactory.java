package com.yoursway.ide.rhtml.core.java;

import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.sse.core.internal.provisional.AbstractAdapterFactory;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapterFactory;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

/**
 * Factory for JSPTranslationAdapters.
 * 
 * @author pavery
 * 
 */
public class JSPTranslationAdapterFactory extends AbstractAdapterFactory {
    
    private JSPTranslationAdapter fAdapter = null;
    
    // for debugging
    static final boolean DEBUG = Boolean.valueOf(
            Platform.getDebugOption("org.eclipse.jst.jsp.core/debug/jsptranslation")).booleanValue(); //$NON-NLS-1$;
    
    public JSPTranslationAdapterFactory() {
        super(IJSPTranslation.class, true);
    }
    
    @Override
    public INodeAdapterFactory copy() {
        return new JSPTranslationAdapterFactory();
    }
    
    @Override
    protected INodeAdapter createAdapter(INodeNotifier target) {
        if (target instanceof IDOMNode && fAdapter == null) {
            fAdapter = new JSPTranslationAdapter(((IDOMNode) target).getModel());
            if (DEBUG) {
                System.out
                        .println("(+) JSPTranslationAdapterFactory [" + this + "] created adapter: " + fAdapter); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return fAdapter;
    }
    
    @Override
    public void release() {
        if (fAdapter != null) {
            if (DEBUG) {
                System.out
                        .println("(-) JSPTranslationAdapterFactory [" + this + "] releasing adapter: " + fAdapter); //$NON-NLS-1$ //$NON-NLS-2$
            }
            fAdapter.release();
        }
        super.release();
    }
}
