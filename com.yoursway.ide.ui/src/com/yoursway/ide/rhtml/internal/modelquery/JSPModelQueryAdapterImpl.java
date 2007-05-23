package com.yoursway.ide.rhtml.internal.modelquery;

import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolver;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.util.CMDocumentCache;
import org.eclipse.wst.xml.core.internal.ssemodelquery.ModelQueryAdapterImpl;

public class JSPModelQueryAdapterImpl extends ModelQueryAdapterImpl {
    public JSPModelQueryAdapterImpl(CMDocumentCache cmDocumentCache, ModelQuery modelQuery,
            URIResolver idResolver) {
        super(cmDocumentCache, modelQuery, idResolver);
    }
}
