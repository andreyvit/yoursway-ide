package com.yoursway.ide.rhtml.core.internal.document;

import org.eclipse.wst.sse.core.internal.ltk.modelhandler.EmbeddedTypeHandler;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapterFactory;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;

/**
 * Classes which implement this interface have two responsibilities. One is to
 * provide and embedded factory registry for JSP Aware INodeAdapter Factories to
 * use. The other is to monitor page directives and if a change in embedded type
 * is is made, it will signal the structuredModel that it needs to reinitialize
 * itself.
 */
public interface PageDirectiveAdapter extends INodeAdapter {
    
    public String getContentType();
    
    public String getLanguage();
    
    /**
     * This setter method should be called once, shortly after initialization.
     */
    void setEmbeddedType(EmbeddedTypeHandler handler);
    
    EmbeddedTypeHandler getEmbeddedType();
    
    /**
     * This method is to give this adapter a chance to use the AdapterFactores
     * from the EmbeddedTypeHandler to adapt the node. Its to be used by
     * JSPAwareAdapterFactories to (potentially) adapt nodes from the embedded
     * content type.
     */
    INodeAdapter adapt(INodeNotifier notifier, Object type);
    
    void addEmbeddedFactory(INodeAdapterFactory factory);
    
    /**
     * Method setLanguage.
     * 
     * @param language
     */
    void setLanguage(String language);
    
    INodeNotifier getTarget();
    
    public void release();
}
