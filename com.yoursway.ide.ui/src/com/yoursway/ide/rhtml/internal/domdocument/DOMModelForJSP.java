package com.yoursway.ide.rhtml.internal.domdocument;

import org.eclipse.wst.html.core.internal.document.DOMStyleModelImpl;
import org.eclipse.wst.xml.core.internal.document.XMLModelParser;
import org.eclipse.wst.xml.core.internal.document.XMLModelUpdater;
import org.w3c.dom.Document;

public class DOMModelForJSP extends DOMStyleModelImpl {
    
    /**
     * 
     */
    public DOMModelForJSP() {
        super();
        // remember, the document is created in super constructor, 
        // via internalCreateDocument
    }
    
    /**
     * createDocument method
     * 
     * @return org.w3c.dom.Document
     */
    @Override
    protected Document internalCreateDocument() {
        DOMDocumentForJSP document = new DOMDocumentForJSP();
        document.setModel(this);
        return document;
    }
    
    @Override
    protected XMLModelParser createModelParser() {
        return new NestedDOMModelParser(this);
    }
    
    @Override
    protected XMLModelUpdater createModelUpdater() {
        return new NestDOMModelUpdater(this);
    }
}
