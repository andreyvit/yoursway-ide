package com.yoursway.ide.rhtml.core.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;

import com.yoursway.ide.rhtml.internal.regions.DOMJSPRegionContexts;
import com.yoursway.ide.ui.Activator;

/**
 * Adds the notion of IDocuments (jsp Document and java Document) Used for
 * TextEdit translation
 * 
 * @author pavery
 */
public class JSPTranslationExtension extends JSPTranslation {
    
    // for debugging
    private static final boolean DEBUG;
    static {
        String value = Platform.getDebugOption("org.eclipse.jst.jsp.core/debug/jsptranslation"); //$NON-NLS-1$
        DEBUG = value != null && value.equalsIgnoreCase("true"); //$NON-NLS-1$
    }
    
    // just a convenience data structure
    // to keep track of java position deltas
    private class PositionDelta {
        
        public boolean isDeleted = false;
        public int preOffset = 0;
        public int preLength = 0;
        public int postOffset = 0;
        public int postLength = 0;
        
        public PositionDelta(int preOffset, int preLength) {
            this.preOffset = preOffset;
            this.preLength = preLength;
        }
        
        public void setPostEditData(int postOffset, int postLength, boolean isDeleted) {
            this.postOffset = postOffset;
            this.postLength = postLength;
            this.isDeleted = isDeleted;
        }
    }
    
    private IDocument fJspDocument = null;
    private IDocument fJavaDocument = null;
    
    public JSPTranslationExtension(IDocument jspDocument, IDocument javaDocument, IScriptProject javaProj,
            JSPTranslator translator) {
        super(javaProj, translator);
        fJspDocument = jspDocument;
        fJavaDocument = javaDocument;
        
        // make sure positions are added to Java and JSP documents
        // this is necessary for text edits
        addPositionsToDocuments();
    }
    
    public IDocument getJspDocument() {
        return fJspDocument;
    }
    
    public IDocument getJavaDocument() {
        return fJavaDocument;
    }
    
    @Override
    public String getJavaText() {
        return getJavaDocument() != null ? getJavaDocument().get() : ""; //$NON-NLS-1$
    }
    
    /**
     * Returns a corresponding TextEdit for the JSP file given a TextEdit for a
     * Java file.
     * 
     * @param javaEdit
     * @return the corresponding JSP edits (not applied to the document yet)
     */
    public TextEdit getJspEdit(TextEdit javaEdit) {
        
        if (javaEdit == null)
            return null;
        
        List jspEdits = new ArrayList();
        
        int offset = javaEdit.getOffset();
        int length = javaEdit.getLength();
        
        if (javaEdit instanceof MultiTextEdit && javaEdit.getChildren().length > 0) {
            
            IRegion r = TextEdit.getCoverage(getAllEdits(javaEdit));
            offset = r.getOffset();
            length = r.getLength();
        }
        
        // get java ranges that will be affected by the edit
        Position[] javaPositions = getJavaRanges(offset, length);
        
        // record position data before the change
        Position[] jspPositions = new Position[javaPositions.length];
        PositionDelta[] deltas = new PositionDelta[javaPositions.length];
        for (int i = 0; i < javaPositions.length; i++) {
            deltas[i] = new PositionDelta(javaPositions[i].offset, javaPositions[i].length);
            // isIndirect means the position doesn't actually exist as exact
            // text
            // mapping from java <-> jsp (eg. an import statement)
            if (!isIndirect(javaPositions[i].offset))
                jspPositions[i] = (Position) getJava2JspMap().get(javaPositions[i]);
        }
        
        if (DEBUG) {
            System.out.println("================================================"); //$NON-NLS-1$
            System.out.println("deltas:"); //$NON-NLS-1$
            String javaText = getJavaText();
            for (int i = 0; i < deltas.length; i++)
                System.out
                        .println("pos[" + deltas[i].preOffset + ":" + deltas[i].preLength + "]" + javaText.substring(deltas[i].preOffset, deltas[i].preOffset + deltas[i].preLength)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("==============================================="); //$NON-NLS-1$
        }
        UndoEdit undo = null;
        // apply the edit to the java document
        try {
            undo = javaEdit.apply(getJavaDocument());
        } catch (MalformedTreeException e) {
            Activator.unexpectedError(e);
        } catch (BadLocationException e) {
            Activator.unexpectedError(e);
        }
        // now at this point Java positions are unreliable since they were
        // updated after applying java edit.
        
        String newJavaText = getJavaDocument().get();
        if (DEBUG)
            System.out.println("java post format text:\n" + newJavaText); //$NON-NLS-1$
            
        // record post edit data
        for (int i = 0; i < javaPositions.length; i++)
            deltas[i].setPostEditData(javaPositions[i].offset, javaPositions[i].length,
                    javaPositions[i].isDeleted);
        
        // create appropriate text edits for deltas
        Position jspPos = null;
        String replaceText = ""; //$NON-NLS-1$
        for (int i = 0; i < deltas.length; i++) {
            jspPos = jspPositions[i];
            // can be null if it's an indirect mapping position
            // or if something was added into java that was not originally in
            // JSP (like a new import...)
            
            if (jspPos != null) {
                if (deltas[i].isDeleted) {
                    jspEdits.add(new DeleteEdit(jspPos.offset, jspPos.length));
                } else {
                    replaceText = newJavaText.substring(deltas[i].postOffset, deltas[i].postOffset
                            + deltas[i].postLength);
                    
                    // get rid of pre and post white space or fine tuned
                    // adjustment later.
                    // fix text here...
                    replaceText = fixJspReplaceText(replaceText, jspPos.offset);
                    
                    jspEdits.add(new ReplaceEdit(jspPos.offset, jspPos.length, replaceText));
                }
                if (DEBUG)
                    debugReplace(deltas, jspPos, replaceText, i);
            } else {
                // the new Java text has no corresponding JSP position
                // possible new import?
                if (isImport(javaPositions[i].getOffset()) && replaceText.lastIndexOf("import ") != -1) { //$NON-NLS-1$
                    replaceText = newJavaText.substring(deltas[i].postOffset, deltas[i].postOffset
                            + deltas[i].postLength);
                    String importText = replaceText.substring(
                            replaceText.lastIndexOf("import "), replaceText.indexOf(";")); //$NON-NLS-1$ //$NON-NLS-2$
                    // evenutally need to check if it's XML-JSP
                    importText = "<%@page import=\"" + importText + "\" %>\n"; //$NON-NLS-1$ //$NON-NLS-2$
                    jspEdits.add(new InsertEdit(0, importText));
                }
            }
        }
        TextEdit allJspEdits = createMultiTextEdit((TextEdit[]) jspEdits
                .toArray(new TextEdit[jspEdits.size()]));
        
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=105632
        // undo the java edit
        // (so the underlying Java document still represents what's in the
        // editor)
        if (undo != null) {
            try {
                undo.apply(getJavaDocument());
            } catch (MalformedTreeException e) {
                Activator.unexpectedError(e);
            } catch (BadLocationException e) {
                Activator.unexpectedError(e);
            }
        }
        
        return allJspEdits;
    }
    
    private String fixJspReplaceText(String replaceText, int jspOffset) {
        
        // result is the text inbetween the delimiters
        // eg.
        // 
        // <% result
        // %>
        String result = replaceText.trim();
        String preDelimiterWhitespace = ""; //$NON-NLS-1$
        
        IDocument jspDoc = getJspDocument();
        if (jspDoc instanceof IStructuredDocument) {
            IStructuredDocument sDoc = (IStructuredDocument) jspDoc;
            IStructuredDocumentRegion[] regions = sDoc.getStructuredDocumentRegions(0, jspOffset);
            IStructuredDocumentRegion lastRegion = regions[regions.length - 1];
            
            // only specifically modify scriptlets
            if (lastRegion != null && lastRegion.getType() == DOMJSPRegionContexts.JSP_SCRIPTLET_OPEN) {
                for (int i = regions.length - 1; i >= 0; i--) {
                    IStructuredDocumentRegion region = regions[i];
                    
                    // is there a better way to check whitespace?
                    if (region.getType() == DOMRegionContext.XML_CONTENT
                            && region.getFullText().trim().equals("")) { //$NON-NLS-1$
                    
                        preDelimiterWhitespace = region.getFullText();
                        preDelimiterWhitespace = preDelimiterWhitespace.replaceAll("\r", ""); //$NON-NLS-1$ //$NON-NLS-2$
                        preDelimiterWhitespace = preDelimiterWhitespace.replaceAll("\n", ""); //$NON-NLS-1$ //$NON-NLS-2$
                        
                        // need to determine indent for that first line...
                        String initialIndent = getInitialIndent(result);
                        
                        // fix the first line of java code
                        result = TextUtilities.getDefaultLineDelimiter(sDoc) + initialIndent + result;
                        
                        result = adjustIndent(result, preDelimiterWhitespace, TextUtilities
                                .getDefaultLineDelimiter(sDoc));
                        
                        // add whitespace before last delimiter to match
                        // it w/ the opening delimiter
                        result = result + TextUtilities.getDefaultLineDelimiter(sDoc)
                                + preDelimiterWhitespace;
                        break;
                    }
                }
            }
        }
        return result;
    }
    
    private String adjustIndent(String textBefore, String indent, String delim) {
        
        // first replace multiple indent with single indent
        // the triple indent occurs because the scriptlet code
        // actually occurs under:
        // 
        // class
        // method
        // code
        // 
        // in the translated java document
        
        textBefore = textBefore.replaceAll("\t\t\t", "\t"); //$NON-NLS-1$ //$NON-NLS-2$
        
        // get indent after 2nd line break
        StringBuffer textAfter = new StringBuffer();
        // will this work on mac?
        textBefore = textBefore.replaceAll("\r", ""); //$NON-NLS-1$ //$NON-NLS-2$
        StringTokenizer st = new StringTokenizer(textBefore, "\n", true); //$NON-NLS-1$
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            if (tok.equals("\n")) { //$NON-NLS-1$
                textAfter.append(delim);
            } else {
                // prepend each line w/ specified indent
                textAfter.append(indent);
                textAfter.append(tok);
            }
        }
        return textAfter.toString();
        
    }
    
    private String getInitialIndent(String result) {
        
        // get indent after 2nd line break
        String indent = ""; //$NON-NLS-1$
        StringTokenizer st = new StringTokenizer(result, "\r\n", false); //$NON-NLS-1$
        if (st.countTokens() > 1) {
            String tok = st.nextToken();
            tok = st.nextToken();
            int index = 0;
            if (tok != null) {
                while (tok.charAt(index) == ' ' || tok.charAt(index) == '\t') {
                    indent += tok.charAt(index);
                    index++;
                }
            }
        }
        return indent;
    }
    
    /**
     * Combines an array of edits into one MultiTextEdit (with the appropriate
     * coverage region)
     * 
     * @param edits
     * @return
     */
    private TextEdit createMultiTextEdit(TextEdit[] edits) {
        
        if (edits.length == 0)
            return new MultiTextEdit();
        
        IRegion region = TextEdit.getCoverage(edits);
        MultiTextEdit multiEdit = new MultiTextEdit(region.getOffset(), region.getLength());
        for (int i = 0; i < edits.length; i++) {
            addToMultiEdit(edits[i], multiEdit);
        }
        return multiEdit;
    }
    
    private void addToMultiEdit(TextEdit edit, MultiTextEdit multiEdit) {
        
        // check for overlap here
        // discard overlapping edits..
        // possible exponential performance hit... need a better way...
        TextEdit[] children = multiEdit.getChildren();
        for (int i = 0; i < children.length; i++) {
            if (children[i].covers(edit))
                // don't add
                return;
        }
        multiEdit.addChild(edit);
    }
    
    /**
     * @param translation
     */
    private void addPositionsToDocuments() {
        
        // can be null if it's a NullJSPTranslation
        if (getJavaDocument() != null && getJspDocument() != null) {
            
            HashMap java2jsp = getJava2JspMap();
            Iterator it = java2jsp.keySet().iterator();
            Position javaPos = null;
            while (it.hasNext()) {
                javaPos = (Position) it.next();
                try {
                    
                    fJavaDocument.addPosition(javaPos);
                    
                } catch (BadLocationException e) {
                    if (DEBUG) {
                        System.out
                                .println("tyring to add Java Position:[" + javaPos.offset + ":" + javaPos.length + "] to " + getJavaPath()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$					
                        // System.out.println("substring :[" +
                        // fJavaDocument.get().substring(javaPos.offset) +
                        // "]"); //$NON-NLS-1$ //$NON-NLS-2$
                        Activator.unexpectedError(e);
                    }
                }
                
                try {
                    
                    fJspDocument.addPosition((Position) java2jsp.get(javaPos));
                    
                } catch (BadLocationException e) {
                    if (DEBUG) {
                        System.out
                                .println("tyring to add JSP Position:[" + ((Position) java2jsp.get(javaPos)).offset + ":" + ((Position) java2jsp.get(javaPos)).length + "] to " + getJavaPath()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        Activator.unexpectedError(e);
                    }
                }
            }
        }
    }
    
    /**
     * Recursively gets all child edits
     * 
     * @param javaEdit
     * @return all child edits
     */
    private TextEdit[] getAllEdits(TextEdit javaEdit) {
        
        List result = new ArrayList();
        if (javaEdit instanceof MultiTextEdit) {
            TextEdit[] children = javaEdit.getChildren();
            for (int i = 0; i < children.length; i++)
                result.addAll(Arrays.asList(getAllEdits(children[i])));
        } else
            result.add(javaEdit);
        return (TextEdit[]) result.toArray(new TextEdit[result.size()]);
    }
    
    /**
     * @param deltas
     * @param jspPos
     * @param replaceText
     * @param jspText
     * @param i
     */
    private void debugReplace(PositionDelta[] deltas, Position jspPos, String replaceText, int i) {
        String jspChunk;
        jspChunk = getJspDocument().get().substring(jspPos.offset, jspPos.offset + jspPos.length);
        if (!deltas[i].isDeleted) {
            System.out.println("replacing:"); //$NON-NLS-1$
            System.out.println("jsp:[" + jspChunk + "]"); //$NON-NLS-1$ //$NON-NLS-2$
            System.out.println("w/ :[" + replaceText + "]"); //$NON-NLS-1$ //$NON-NLS-2$
            System.out.println("--------------------------------"); //$NON-NLS-1$
        }
    }
}
