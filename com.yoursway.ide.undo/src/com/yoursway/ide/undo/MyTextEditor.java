package com.yoursway.ide.undo;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

public class MyTextEditor extends TextEditor {
    
    public MyTextEditor() {
        super();
        //setDocumentProvider(new TextFileDocumentProvider());
    }
    
    @Override
    public void dispose() {
        super.dispose();
    }
    
    //> override SetDocumentProvider
    
    @Override
    protected void doSetInput(IEditorInput input) throws CoreException {
        super.doSetInput(input);
        
        IDocument document = getDocumentProvider().getDocument(input);
        IDocumentListener listener = new DocumentListener(this);
        document.addDocumentListener(listener);
        
        DocumentsManager.get().add(input, document);
    }
    
}
