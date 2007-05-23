package com.yoursway.ide.rhtml.internal.domdocument;

import org.eclipse.wst.xml.core.internal.document.CommentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.yoursway.ide.rhtml.internal.regions.DOMJSPRegionContexts;

/**
 * CommentImplForJSP
 */
public class CommentImplForJSP extends CommentImpl {
    protected CommentImplForJSP() {
        super();
    }
    
    protected CommentImplForJSP(CommentImpl that) {
        super(that);
    }
    
    @Override
    protected boolean isNestedCommentClose(String regionType) {
        boolean result = regionType == DOMJSPRegionContexts.JSP_COMMENT_CLOSE;
        return result;
    }
    
    @Override
    protected boolean isNestedCommentOpenClose(String regionType) {
        boolean result = regionType == DOMJSPRegionContexts.JSP_COMMENT_OPEN
                || regionType == DOMJSPRegionContexts.JSP_COMMENT_CLOSE;
        return result;
    }
    
    @Override
    protected void setOwnerDocument(Document ownerDocument) {
        super.setOwnerDocument(ownerDocument);
    }
    
    @Override
    public Node cloneNode(boolean deep) {
        CommentImpl cloned = new CommentImplForJSP(this);
        return cloned;
    }
}
