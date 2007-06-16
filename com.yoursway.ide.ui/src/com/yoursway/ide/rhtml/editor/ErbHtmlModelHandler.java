package com.yoursway.ide.rhtml.editor;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.wst.sse.core.internal.document.IDocumentCharsetDetector;
import org.eclipse.wst.sse.core.internal.document.IDocumentLoader;
import org.eclipse.wst.sse.core.internal.ltk.modelhandler.AbstractModelHandler;
import org.eclipse.wst.sse.core.internal.provisional.IModelLoader;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapterFactory;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

import com.yoursway.ide.rhtml.core.java.IJSPTranslation;
import com.yoursway.ide.rhtml.core.java.JSPTranslationAdapterFactory;
import com.yoursway.ide.rhtml.internal.contenttype.ContentTypeIdForJSP;
import com.yoursway.ide.rhtml.internal.encoding.JSPDocumentHeadContentDetector;
import com.yoursway.ide.rhtml.internal.encoding.JSPDocumentLoader;
import com.yoursway.ide.rhtml.internal.modelhandler.JSPModelLoader;
import com.yoursway.ide.ui.Activator;

@SuppressWarnings("restriction")
public class ErbHtmlModelHandler extends AbstractModelHandler {
    
    static String AssociatedContentTypeID = ContentTypeIdForJSP.ContentTypeID_JSP; //$NON-NLS-1$
    private static String ModelHandlerID = "com.yoursway.erbhtml.modelhandler"; //$NON-NLS-1$
    
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
    
    public static void ensureTranslationAdapterFactory(IStructuredModel sm) {
        if (sm.getFactoryRegistry().getFactoryFor(IJSPTranslation.class) == null) {
            INodeAdapterFactory factory = null;
            //              if (false) {
            //                  IContentType textContentType = Platform.getContentTypeManager().getContentType(IContentTypeManager.CT_TEXT);
            //                  IContentType jspContentType = Platform.getContentTypeManager().getContentType(ContentTypeIdForJSP.ContentTypeID_JSP);
            //                  /*
            //                   * This IAdapterManager call is temporary placeholder code
            //                   * that should not be relied upon in any way!
            //                   */
            //                  if (thisContentType.isKindOf(jspContentType)) {
            //                      IContentType testContentType = thisContentType;
            //                      INodeAdapterFactory holdFactory = null;
            //                      while (!testContentType.equals(textContentType) && holdFactory == null) {
            //                          holdFactory = (INodeAdapterFactory) Platform.getAdapterManager().getAdapter(testContentType.getId(), IJSPTranslation.class.getName());
            //                          testContentType = testContentType.getBaseType();
            //                      }
            //                  }
            //              }
            if (factory == null) {
                factory = new JSPTranslationAdapterFactory();
            }
            
            sm.getFactoryRegistry().addFactory(factory);
        }
    }
}
