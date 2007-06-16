package com.yoursway.ide.rhtml.core.java;

import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapterFactory;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

public class TagTranslationAdapterFactory extends JSPTranslationAdapterFactory {
    
    private TagTranslationAdapter fAdapter = null;
    
    public TagTranslationAdapterFactory() {
        super();
    }
    
    @Override
    public INodeAdapterFactory copy() {
        return new TagTranslationAdapterFactory();
    }
    
    @Override
    protected INodeAdapter createAdapter(INodeNotifier target) {
        if (target instanceof IDOMNode && fAdapter == null) {
            fAdapter = new TagTranslationAdapter(((IDOMNode) target).getModel());
            if (DEBUG) {
                System.out
                        .println("(+) TagTranslationAdapterFactory [" + this + "] created adapter: " + fAdapter); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return fAdapter;
    }
    
    @Override
    public void release() {
        if (fAdapter != null) {
            if (DEBUG) {
                System.out
                        .println("(-) TagTranslationAdapterFactory [" + this + "] releasing adapter: " + fAdapter); //$NON-NLS-1$ //$NON-NLS-2$
            }
            fAdapter.release();
        }
        super.release();
    }
}
