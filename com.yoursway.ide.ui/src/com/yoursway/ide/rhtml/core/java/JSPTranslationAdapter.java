package com.yoursway.ide.rhtml.core.java;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.ruby.core.RubyPlugin;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

import com.yoursway.ide.ui.Activator;

/**
 * An adapter for getting a JSPTranslation of the document.
 * 
 * @author pavery
 */
@SuppressWarnings("restriction")
public class JSPTranslationAdapter implements INodeAdapter, IDocumentListener {
    
    // for debugging
    private static final boolean DEBUG = "true".equalsIgnoreCase(Platform.getDebugOption("org.eclipse.jst.jsp.core/debug/jsptranslation")); //$NON-NLS-1$  //$NON-NLS-2$
    
    private IDocument fJspDocument = null;
    private IDocument fJavaDocument = null;
    private JSPTranslationExtension fJSPTranslation = null;
    private boolean fDocumentIsDirty = true;
    private IDOMModel fXMLModel;
    private JSPTranslator fTranslator = null;
    private NullProgressMonitor fTranslationMonitor = null;
    
    public JSPTranslationAdapter(IDOMModel xmlModel) {
        setXMLModel(xmlModel);
        initializeJavaPlugins();
    }
    
    /**
     * Initialize the required Java Plugins
     */
    protected void initializeJavaPlugins() {
        RubyPlugin.getDefault();
    }
    
    public boolean isAdapterForType(Object type) {
        return type.equals(IJSPTranslation.class);
    }
    
    public void notifyChanged(INodeNotifier notifier, int eventType, Object changedFeature, Object oldValue,
            Object newValue, int pos) {
        // nothing to do
    }
    
    /**
     * Automatically set through the setXMLModel(XMLModel)
     * 
     * @param doc
     */
    private void setDocument(IDocument doc) {
        if (fJspDocument != null)
            fJspDocument.removeDocumentListener(this);
        if (doc != null) {
            doc.addDocumentListener(this);
            fJspDocument = doc;
        }
    }
    
    /**
     * @see org.eclipse.jface.text.IDocumentListener#documentAboutToBeChanged(org.eclipse.jface.text.DocumentEvent)
     */
    public void documentAboutToBeChanged(DocumentEvent event) {
        // do nothing
    }
    
    /**
     * @see org.eclipse.jface.text.IDocumentListener#documentChanged(org.eclipse.jface.text.DocumentEvent)
     */
    public void documentChanged(DocumentEvent event) {
        // mark translation for rebuilding
        fDocumentIsDirty = true;
    }
    
    public void release() {
        
        if (fJspDocument != null)
            fJspDocument.removeDocumentListener(this);
        
        if (fTranslationMonitor != null)
            fTranslationMonitor.setCanceled(true);
        
        if (fJSPTranslation != null) {
            
            if (DEBUG)
                System.out.println("JSPTranslationAdapter releasing:" + fJSPTranslation); //$NON-NLS-1$
                
            fJSPTranslation.release();
        }
    }
    
    /**
     * Returns the JSPTranslation for this adapter.
     * 
     * @return a JSPTranslationExtension
     */
    public synchronized JSPTranslationExtension getJSPTranslation() {
        
        if (fJSPTranslation == null || fDocumentIsDirty) {
            JSPTranslator translator = null;
            if (getXMLModel() != null && getXMLModel().getIndexedRegion(0) != null) {
                translator = getTranslator((IDOMNode) getXMLModel().getIndexedRegion(0));
                translator.translate();
                StringBuffer javaContents = translator.getTranslation();
                fJavaDocument = new Document(javaContents.toString());
            } else {
                // empty document case
                translator = createTranslator();
                StringBuffer emptyContents = translator.getEmptyTranslation();
                fJavaDocument = new Document(emptyContents.toString());
            }
            // it's going to be rebuilt, so we release it here
            if (fJSPTranslation != null) {
                if (DEBUG)
                    System.out.println("JSPTranslationAdapter releasing:" + fJSPTranslation); //$NON-NLS-1$
                fJSPTranslation.release();
            }
            fJSPTranslation = new JSPTranslationExtension(getXMLModel().getStructuredDocument(),
                    fJavaDocument, getJavaProject(), translator);
            fDocumentIsDirty = false;
        }
        return fJSPTranslation;
    }
    
    JSPTranslator createTranslator() {
        return new JSPTranslator();
    }
    
    /**
     * Returns the JSPTranslator for this adapter. If it's null, a new
     * translator is created with the xmlNode. Otherwise the
     * translator.reset(xmlNode) is called to reset the current local
     * translator.
     * 
     * @param xmlNode
     *            the first node of the JSP document to be translated
     * @return the JSPTranslator for this adapter (creates if null)
     */
    private JSPTranslator getTranslator(IDOMNode xmlNode) {
        if (fTranslator == null) {
            fTranslationMonitor = new NullProgressMonitor();
            fTranslator = createTranslator();
            fTranslator.reset(xmlNode, fTranslationMonitor);
        } else
            fTranslator.reset(xmlNode, fTranslationMonitor);
        return fTranslator;
    }
    
    /**
     * set the XMLModel for this adapter. Must be called.
     * 
     * @param xmlModel
     */
    public void setXMLModel(IDOMModel xmlModel) {
        fXMLModel = xmlModel;
        setDocument(fXMLModel.getStructuredDocument());
    }
    
    /**
     * @return the XMLModel for this adapter.
     */
    private IDOMModel getXMLModel() {
        return fXMLModel;
    }
    
    /**
     * Gets (or creates via JavaCore) a JavaProject based on the location of
     * this adapter's XMLModel. Returns null for non IFile based models.
     * 
     * @return the java project where
     */
    public IScriptProject getJavaProject() {
        
        IScriptProject javaProject = null;
        try {
            String baseLocation = getXMLModel().getBaseLocation();
            // 20041129 (pa) the base location changed for XML model
            // because of FileBuffers, so this code had to be updated
            // https://bugs.eclipse.org/bugs/show_bug.cgi?id=79686
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IPath filePath = new Path(baseLocation);
            IProject project = null;
            if (filePath.segmentCount() > 0) {
                project = root.getProject(filePath.segment(0));
            }
            //			IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(new Path(baseLocation));
            //			for (int i = 0; project == null && i < files.length; i++) {
            //				if (files[i].getType() != IResource.PROJECT) {
            //					project = files[i].getProject();
            //					break;
            //				}
            //			}
            if (project != null) {
                javaProject = DLTKCore.create(project);
            }
        } catch (Exception ex) {
            if (getXMLModel() != null)
                Activator
                        .unexpectedError(
                                "(JSPTranslationAdapter) problem getting java project from the XMLModel's baseLocation > " + getXMLModel().getBaseLocation(), ex); //$NON-NLS-1$
            else
                Activator.unexpectedError("(JSPTranslationAdapter) problem getting java project", ex); //$NON-NLS-1$
        }
        return javaProject;
    }
}
