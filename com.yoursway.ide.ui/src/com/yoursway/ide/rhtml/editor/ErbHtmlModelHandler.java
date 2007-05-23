package com.yoursway.ide.rhtml.editor;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.wst.sse.core.internal.document.IDocumentCharsetDetector;
import org.eclipse.wst.sse.core.internal.document.IDocumentLoader;
import org.eclipse.wst.sse.core.internal.ltk.modelhandler.AbstractModelHandler;
import org.eclipse.wst.sse.core.internal.provisional.IModelLoader;

import com.yoursway.ide.rhtml.internal.encoding.JSPDocumentHeadContentDetector;
import com.yoursway.ide.rhtml.internal.encoding.JSPDocumentLoader;
import com.yoursway.ide.rhtml.internal.modelhandler.JSPModelLoader;
import com.yoursway.ide.ui.Activator;

@SuppressWarnings("restriction")
public class ErbHtmlModelHandler extends AbstractModelHandler {
    
    static String AssociatedContentTypeID = "org.eclipse.jst.jsp.core.jspsource"; //$NON-NLS-1$
    private static String ModelHandlerID = "org.eclipse.jst.jsp.core.modelhandler"; //$NON-NLS-1$
    
    public ErbHtmlModelHandler() {
        super();
        setId(ModelHandlerID);
        setAssociatedContentTypeId(AssociatedContentTypeID);
    }
    
//    protected void addJSPTagName(JSPSourceParser parser, String tagname) {
//        BlockMarker bm = new BlockMarker(tagname, null, DOMJSPRegionContexts.JSP_CONTENT, true);
//        parser.addBlockMarker(bm);
//    }
    
    public IModelLoader getModelLoader() {
        return new JSPModelLoader();
    }
    
    public Preferences getPreferences() {
        return Activator.getDefault().getPluginPreferences();
    }
    
    @Override
    public IDocumentCharsetDetector getEncodingDetector() {
        return new JSPDocumentHeadContentDetector();
    }
    
    public IDocumentLoader getDocumentLoader() {
        return new JSPDocumentLoader();
    }
    
}
