package com.yoursway.ide.undo;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;

public class DocumentListener implements IDocumentListener {
    
    private String replacedText;
    private final MyTextEditor editor;
    
    public DocumentListener(MyTextEditor editor) {
        this.editor = editor;
    }
    
    public void documentAboutToBeChanged(DocumentEvent event) {
        try {
            replacedText = event.getDocument().get(event.getOffset(), event.getLength());
            //? preservedUndoModificationStamp = event.getModificationStamp();
        } catch (BadLocationException x) {
            replacedText = null;
        }
    }
    
    public void documentChanged(DocumentEvent event) {
        OperationHistory history = OperationHistory.get();
        IUndoableOperation operation = new UndoableTextChange(event, replacedText);
        history.execute(operation);
        
        editor.doSave(null);
    }
}
