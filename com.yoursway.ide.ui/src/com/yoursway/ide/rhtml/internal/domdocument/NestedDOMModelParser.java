package com.yoursway.ide.rhtml.internal.domdocument;

import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.xml.core.internal.document.DOMModelImpl;
import org.eclipse.wst.xml.core.internal.document.JSPTag;
import org.eclipse.wst.xml.core.internal.document.XMLModelParser;

import com.yoursway.ide.rhtml.internal.regions.DOMJSPRegionContexts;

public class NestedDOMModelParser extends XMLModelParser {
    
    /**
     * @param model
     */
    public NestedDOMModelParser(DOMModelImpl model) {
        super(model);
    }
    
    @Override
    protected boolean isNestedCommentOpen(String regionType) {
        boolean result = regionType == DOMJSPRegionContexts.JSP_COMMENT_OPEN;
        return result;
    }
    
    @Override
    protected boolean isNestedCommentText(String regionType) {
        boolean result = regionType == DOMJSPRegionContexts.JSP_COMMENT_TEXT;
        return result;
    }
    
    @Override
    protected boolean isNestedContent(String regionType) {
        boolean result = regionType == DOMJSPRegionContexts.JSP_CONTENT;
        return result;
    }
    
    @Override
    protected boolean isNestedTag(String regionType) {
        boolean result = regionType == DOMJSPRegionContexts.JSP_SCRIPTLET_OPEN
                || regionType == DOMJSPRegionContexts.JSP_EXPRESSION_OPEN
                || regionType == DOMJSPRegionContexts.JSP_DECLARATION_OPEN
                || regionType == DOMJSPRegionContexts.JSP_DIRECTIVE_OPEN
                || regionType == DOMJSPRegionContexts.JSP_CLOSE;
        return result;
    }
    
    @Override
    protected boolean isNestedTagName(String regionType) {
        boolean result = regionType == DOMJSPRegionContexts.JSP_ROOT_TAG_NAME
                || regionType == DOMJSPRegionContexts.JSP_DIRECTIVE_NAME;
        return result;
    }
    
    @Override
    protected boolean isNestedTagOpen(String regionType) {
        boolean result = regionType == DOMJSPRegionContexts.JSP_SCRIPTLET_OPEN
                || regionType == DOMJSPRegionContexts.JSP_EXPRESSION_OPEN
                || regionType == DOMJSPRegionContexts.JSP_DECLARATION_OPEN
                || regionType == DOMJSPRegionContexts.JSP_DIRECTIVE_OPEN;
        return result;
    }
    
    @Override
    protected String computeNestedTag(String regionType, String tagName,
            IStructuredDocumentRegion structuredDocumentRegion, ITextRegion region) {
        String resultTagName = tagName;
        if (regionType == DOMJSPRegionContexts.JSP_SCRIPTLET_OPEN) {
            resultTagName = JSPTag.JSP_SCRIPTLET;
        } else if (regionType == DOMJSPRegionContexts.JSP_EXPRESSION_OPEN) {
            resultTagName = JSPTag.JSP_EXPRESSION;
        } else if (regionType == DOMJSPRegionContexts.JSP_DECLARATION_OPEN) {
            resultTagName = JSPTag.JSP_DECLARATION;
        } else if (regionType == DOMJSPRegionContexts.JSP_DIRECTIVE_OPEN) {
            resultTagName = JSPTag.JSP_DIRECTIVE;
        } else if (regionType == DOMJSPRegionContexts.JSP_DIRECTIVE_NAME) {
            resultTagName += '.';
            resultTagName += structuredDocumentRegion.getText(region);
        }
        return resultTagName;
    }
    
    @Override
    protected boolean isNestedTagClose(String regionType) {
        boolean result = regionType == DOMJSPRegionContexts.JSP_CLOSE
                || regionType == DOMJSPRegionContexts.JSP_DIRECTIVE_CLOSE;
        return result;
    }
    
}
