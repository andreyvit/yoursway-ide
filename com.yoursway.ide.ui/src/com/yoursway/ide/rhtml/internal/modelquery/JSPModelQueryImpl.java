package com.yoursway.ide.rhtml.internal.modelquery;

import java.util.HashMap;
import java.util.List;

import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolver;
import org.eclipse.wst.html.core.internal.provisional.contenttype.ContentTypeFamilyForHTML;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelqueryimpl.ModelQueryImpl;
import org.eclipse.wst.xml.core.internal.contentmodel.modelqueryimpl.SimpleAssociationProvider;
import org.eclipse.wst.xml.core.internal.ssemodelquery.ModelQueryAdapter;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.yoursway.ide.rhtml.core.internal.document.PageDirectiveAdapter;

public class JSPModelQueryImpl extends ModelQueryImpl {
    
    // ISSUE: jspModel doesn't seem used?
    protected IStructuredModel jspModel = null;
    private final HashMap embeddedModelQueries = new HashMap();
    
    public JSPModelQueryImpl(IStructuredModel model, URIResolver resolver) {
        super(new SimpleAssociationProvider(new JSPModelQueryCMProvider()));
        jspModel = model;
    }
    
    /*
     * @see ModelQuery#getCMElementDeclaration(Element)
     */
    @Override
    public CMElementDeclaration getCMElementDeclaration(Element element) {
        CMElementDeclaration result = super.getCMElementDeclaration(element);
        if (result == null) {
            ModelQuery query = getEmbeddedModelQuery(element);
            if (query != null) {
                result = query.getCMElementDeclaration(element);
            }
        }
        return result;
    }
    
    /*
     * @see ModelQuery#getCorrespondingCMDocument(Node)
     */
    @Override
    public CMDocument getCorrespondingCMDocument(Node node) {
        CMDocument doc = super.getCorrespondingCMDocument(node);
        if (doc == null) {
            ModelQuery query = getEmbeddedModelQuery(node);
            if (query != null) {
                doc = query.getCorrespondingCMDocument(node);
            }
        }
        return doc;
    }
    
    /*
     * @see ModelQuery#getCMNode(Node)
     */
    @Override
    public CMNode getCMNode(Node node) {
        CMNode result = super.getCMNode(node);
        if (result == null) {
            ModelQuery query = getEmbeddedModelQuery(node);
            if (query != null) {
                result = query.getCMNode(node);
            }
        }
        return result;
    }
    
    @Override
    public List getAvailableContent(Element element, CMElementDeclaration ed, int includeOptions) {
        ModelQuery emq = getEmbeddedModelQuery(element);
        if (emq != null)
            return emq.getAvailableContent(element, ed, includeOptions);
        else
            return super.getAvailableContent(element, ed, includeOptions);
    }
    
    // ISSUE: shouldn't this be private?
    protected ModelQuery getEmbeddedModelQuery(Node node) {
        ModelQuery embeddedModelQuery = null;
        
        if (node instanceof INodeNotifier) {
            Node ownerNode = node.getOwnerDocument();
            if (ownerNode == null) {
                // then must be the document itself
                ownerNode = node;
            }
            PageDirectiveAdapter pageDirectiveAdapter = (PageDirectiveAdapter) ((INodeNotifier) ownerNode)
                    .getAdapterFor(PageDirectiveAdapter.class);
            if (pageDirectiveAdapter != null) {
                
                String effectiveContentType = null;
                ModelQuery potentialModelQueryObject = null;
                
                String familyId = pageDirectiveAdapter.getEmbeddedType().getFamilyId();
                if (ContentTypeFamilyForHTML.HTML_FAMILY.equals(familyId)) {
                    effectiveContentType = "text/html";
                } else {
                    effectiveContentType = pageDirectiveAdapter.getContentType();
                }
                
                potentialModelQueryObject = (ModelQuery) embeddedModelQueries.get(effectiveContentType);
                
                if (potentialModelQueryObject == null) {
                    ModelQueryAdapter embeddedAdapter = (ModelQueryAdapter) pageDirectiveAdapter.adapt(
                            (INodeNotifier) node, ModelQueryAdapter.class);
                    if (embeddedAdapter != null) {
                        // we will cache one model query per content type
                        embeddedModelQuery = embeddedAdapter.getModelQuery();
                        embeddedModelQueries.put(effectiveContentType, embeddedModelQuery);
                    }
                } else {
                    embeddedModelQuery = potentialModelQueryObject;
                }
            }
        }
        return embeddedModelQuery;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery#getCMAttributeDeclaration(org.w3c.dom.Attr)
     */
    @Override
    public CMAttributeDeclaration getCMAttributeDeclaration(Attr attr) {
        CMAttributeDeclaration result = super.getCMAttributeDeclaration(attr);
        if (result == null) {
            ModelQuery query = getEmbeddedModelQuery(attr);
            if (query != null) {
                result = query.getCMAttributeDeclaration(attr);
            }
        }
        return result;
    }
    
    /**
     * NOT API -- this is provided, and is public, only to make some JUnit
     * testing more straightforward. It will be changed in future, and from
     * release to release.
     * 
     * @param node
     * @return
     */
    public ModelQuery internalTestOnly_getEmbeddedModelQuery(Node node) {
        return getEmbeddedModelQuery(node);
    }
}
