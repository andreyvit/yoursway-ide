package com.yoursway.ide.rhtml.internal.domdocument;

import org.eclipse.wst.html.core.internal.document.DocumentStyleImpl;
import org.eclipse.wst.xml.core.internal.document.DocumentImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

@SuppressWarnings("restriction")
public class DOMDocumentForJSP extends DocumentStyleImpl {
    
    /**
     * 
     */
    public DOMDocumentForJSP() {
        super();
    }
    
    /**
     * @param that
     */
    protected DOMDocumentForJSP(DocumentImpl that) {
        super(that);
    }
    
    /**
     * cloneNode method
     * 
     * @return org.w3c.dom.Node
     * @param deep
     *            boolean
     */
    @Override
    public Node cloneNode(boolean deep) {
        DOMDocumentForJSP cloned = new DOMDocumentForJSP(this);
        if (deep)
            cloned.importChildNodes(this, true);
        return cloned;
    }
    
    /**
     * createElement method
     * 
     * @return org.w3c.dom.Element
     * @param tagName
     *            java.lang.String
     */
    @Override
    public Element createElement(String tagName) throws DOMException {
        checkTagNameValidity(tagName);
        
        ElementImplForJSP element = new ElementImplForJSP();
        element.setOwnerDocument(this);
        element.setTagName(tagName);
        return element;
    }
    
    /**
     * createComment method
     * 
     * @return org.w3c.dom.Comment
     * @param data
     *            java.lang.String
     */
    @Override
    public Comment createComment(String data) {
        CommentImplForJSP comment = new CommentImplForJSP();
        comment.setOwnerDocument(this);
        if (data != null)
            comment.setData(data);
        return comment;
    }
    
    /**
     * createAttribute method
     * 
     * @return org.w3c.dom.Attr
     * @param name
     *            java.lang.String
     */
    @Override
    public Attr createAttribute(String name) throws DOMException {
        AttrImplForJSP attr = new AttrImplForJSP();
        attr.setOwnerDocument(this);
        attr.setName(name);
        return attr;
    }
    
    /**
     */
    @Override
    public Attr createAttributeNS(String uri, String name) throws DOMException {
        AttrImplForJSP attr = new AttrImplForJSP();
        attr.setOwnerDocument(this);
        attr.setName(name);
        attr.setNamespaceURI(uri);
        return attr;
    }
    
    /**
     * createTextNode method
     * 
     * @return org.w3c.dom.Text
     * @param data
     *            java.lang.String
     */
    @Override
    public Text createTextNode(String data) {
        TextImplForJSP text = new TextImplForJSP();
        text.setOwnerDocument(this);
        text.setData(data);
        return text;
    }
    
    @Override
    protected void setModel(IDOMModel model) {
        super.setModel(model);
    }
    
    @Override
    public DocumentType getDoctype() {
        DocumentType doctype = super.getDoctype();
        //        if (doctype == null) {
        //            doctype = createDoctype("HTML");
        //        }
        return doctype;
    }
    
}
