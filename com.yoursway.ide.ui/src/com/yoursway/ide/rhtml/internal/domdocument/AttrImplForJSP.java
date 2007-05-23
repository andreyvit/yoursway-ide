package com.yoursway.ide.rhtml.internal.domdocument;

import org.eclipse.wst.xml.core.internal.document.AttrImpl;
import org.w3c.dom.Document;

import com.yoursway.ide.rhtml.internal.regions.DOMJSPRegionContexts;

public class AttrImplForJSP extends AttrImpl {
    
    @Override
    protected boolean isNestedLanguageOpening(String regionType) {
        boolean result = regionType == DOMJSPRegionContexts.JSP_SCRIPTLET_OPEN
                || regionType == DOMJSPRegionContexts.JSP_EXPRESSION_OPEN
                || regionType == DOMJSPRegionContexts.JSP_DECLARATION_OPEN
                || regionType == DOMJSPRegionContexts.JSP_DIRECTIVE_OPEN;
        return result;
    }
    
    @Override
    protected void setOwnerDocument(Document ownerDocument) {
        super.setOwnerDocument(ownerDocument);
    }
    
    @Override
    protected void setName(String name) {
        super.setName(name);
    }
    
    @Override
    protected void setNamespaceURI(String namespaceURI) {
        super.setNamespaceURI(namespaceURI);
    }
    
}
