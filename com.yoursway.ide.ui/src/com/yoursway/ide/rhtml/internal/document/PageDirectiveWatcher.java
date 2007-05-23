package com.yoursway.ide.rhtml.internal.document;

import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;

interface PageDirectiveWatcher extends INodeAdapter {
    
    String getContentType();
    
    String getLanguage();
    
    int getOffset();
    
}
