package com.yoursway.ide.undo;

import java.util.HashMap;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;

public class DocumentsManager {
    
    private static DocumentsManager instance;
    
    private HashMap<IEditorInput, IDocument> inputsToDocument = new HashMap<IEditorInput, IDocument>();
    private HashMap<IDocument, IEditorInput> documentsToInput = new HashMap<IDocument, IEditorInput>();

    public static DocumentsManager get() {
        if (instance == null)
            instance = new DocumentsManager();
        return instance;
    }

    public void add(IEditorInput input, IDocument document) {
        inputsToDocument.put(input, document);
        documentsToInput.put(document, input);        
    }
    
    public IDocument document(IEditorInput input) {
        return inputsToDocument.get(input);
    }
    
    public IEditorInput input(IDocument document) {
        return documentsToInput.get(document);
    }
    
}
