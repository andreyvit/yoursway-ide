package com.yoursway.ide.rhtml.internal.domdocument;

import org.eclipse.wst.xml.core.internal.document.TextImpl;
import org.w3c.dom.Document;

import com.yoursway.ide.rhtml.internal.regions.DOMJSPRegionContexts;

public class TextImplForJSP extends TextImpl {
    @Override
    protected boolean isNotNestedContent(String regionType) {
        boolean result = regionType != DOMJSPRegionContexts.JSP_CONTENT;
        return result;
    }
    
    @Override
    protected void setOwnerDocument(Document ownerDocument) {
        super.setOwnerDocument(ownerDocument);
    }
    
}
