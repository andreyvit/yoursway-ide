package com.yoursway.ide.undo;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;

public class MyTextEditor extends TextEditor {
    
    public MyTextEditor() {
        super();
        //setDocumentProvider(new TextFileDocumentProvider());
        
        /*
        IDocumentProvider provider = getDocumentProvider();
        DocumentsManager.get().setDocumentProvider(provider);*/
    }
    
    @Override
    public void dispose() {
        super.dispose();
    }
    
    //> override SetDocumentProvider
    
    @Override
    protected void doSetInput(IEditorInput input) throws CoreException {
        super.doSetInput(input);
        
        IDocumentProvider provider = getDocumentProvider();
        IDocument document = provider.getDocument(input);
        IDocumentListener listener = new DocumentListener(this);
        document.addDocumentListener(listener);
                
        
        //> use DocumentProvider instead of DocumentsManager
        DocumentsManager.get().add(input, document);
    }
    
}
