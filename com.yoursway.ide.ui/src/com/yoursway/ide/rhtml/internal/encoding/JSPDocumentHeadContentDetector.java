package com.yoursway.ide.rhtml.internal.encoding;

import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.internal.document.DocumentReader;

import com.yoursway.ide.rhtml.internal.contenttype.JSPResourceEncodingDetector;

/**
 * This class parses beginning portion of JSP file to get attributes in page
 * directiive
 * 
 */
public class JSPDocumentHeadContentDetector extends JSPResourceEncodingDetector implements
        IJSPHeadContentDetector {
    
    public JSPDocumentHeadContentDetector() {
        super();
    }
    
    public void set(IDocument document) {
        set(new DocumentReader(document, 0));
        
    }
    
}
