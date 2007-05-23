package com.yoursway.ide.rhtml.internal.encoding;

import java.io.IOException;

import org.eclipse.wst.sse.core.internal.document.IDocumentCharsetDetector;

public interface IJSPHeadContentDetector extends IDocumentCharsetDetector {
    String getContentType() throws IOException;
    
    String getLanguage() throws IOException;
    
}
