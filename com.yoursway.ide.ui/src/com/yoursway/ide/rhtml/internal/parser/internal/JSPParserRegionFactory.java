package com.yoursway.ide.rhtml.internal.parser.internal;

import org.eclipse.wst.sse.core.internal.parser.ForeignRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.xml.core.internal.parser.regions.XMLParserRegionFactory;

import com.yoursway.ide.rhtml.internal.regions.DOMJSPRegionContexts;

/**
 * 
 * This region factory is very specific to the parser output, and the specific
 * implementation classes for various regions.
 */
public class JSPParserRegionFactory extends XMLParserRegionFactory {
    public JSPParserRegionFactory() {
        super();
    }
    
    @Override
    public ITextRegion createToken(String context, int start, int textLength, int length, String lang,
            String surroundingTag) {
        ITextRegion newRegion = null;
        if (context == DOMJSPRegionContexts.JSP_CONTENT) {
            newRegion = new ForeignRegion(context, start, textLength, length);
        } else
            newRegion = super.createToken(context, start, textLength, length, lang, surroundingTag);
        return newRegion;
    }
}
