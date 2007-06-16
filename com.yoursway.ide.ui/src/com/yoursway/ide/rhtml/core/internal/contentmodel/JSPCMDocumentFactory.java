package com.yoursway.ide.rhtml.core.internal.contentmodel;

import org.eclipse.wst.html.core.internal.contentmodel.HTMLCMDocumentFactory;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.provisional.contentmodel.CMDocType;

/**
 * CMDocument factory for JSP documents (which for now live in the HTML Core
 * plugin).
 */
public final class JSPCMDocumentFactory {
    
    private JSPCMDocumentFactory() {
        super();
    }
    
    public static CMDocument getCMDocument() {
        return getCMDocument(CMDocType.JSP20_DOC_TYPE);
    }
    
    /**
     * @return org.eclipse.wst.xml.core.internal.contentmodel.CMDocument
     * @param cmtype
     *            java.lang.String
     */
    public static CMDocument getCMDocument(String cmtype) {
        if (cmtype == null)
            return getCMDocument();
        return HTMLCMDocumentFactory.getCMDocument(cmtype);
    }
    
    public static CMDocument getCMDocument(float jspVersion) {
        if (jspVersion >= 2)
            return getCMDocument(CMDocType.JSP20_DOC_TYPE);
        if (jspVersion >= 1.2)
            return getCMDocument(CMDocType.JSP12_DOC_TYPE);
        if (jspVersion >= 1)
            return getCMDocument(CMDocType.JSP11_DOC_TYPE);
        return getCMDocument();
    }
}
