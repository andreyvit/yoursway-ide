package com.yoursway.ide.rhtml.core.internal.document;

import org.eclipse.wst.sse.core.internal.PropagatingAdapterFactory;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapterFactory;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.xml.core.internal.propagate.PropagatingAdapterFactoryImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.w3c.dom.Node;

public class PageDirectiveWatcherFactory extends PropagatingAdapterFactoryImpl implements
        PropagatingAdapterFactory {
    
    /**
     * Constructor for PageDirectiveWatcherFactory.
     */
    public PageDirectiveWatcherFactory() {
        this(PageDirectiveWatcher.class, true);
    }
    
    /**
     * Constructor for PageDirectiveWatcherFactory.
     * 
     * @param adapterKey
     * @param registerAdapters
     */
    public PageDirectiveWatcherFactory(Object adapterKey, boolean registerAdapters) {
        super(adapterKey, registerAdapters);
    }
    
    @Override
    protected INodeAdapter createAdapter(INodeNotifier target) {
        PageDirectiveWatcher result = null;
        if (target instanceof IDOMElement) {
            IDOMElement xmlElement = (IDOMElement) target;
            if (xmlElement.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = xmlElement.getNodeName();
                if (nodeName.equals("jsp:directive.page")) { //$NON-NLS-1$
                    result = new PageDirectiveWatcherImpl(xmlElement);
                }
                
            }
        }
        return result;
        
    }
    
    @Override
    public INodeAdapterFactory copy() {
        return new PageDirectiveWatcherFactory(getAdapterKey(), isShouldRegisterAdapter());
    }
}
