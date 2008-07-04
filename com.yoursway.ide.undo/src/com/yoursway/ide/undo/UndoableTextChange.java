package com.yoursway.ide.undo;

import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.ui.IMemento;

public class UndoableTextChange implements IUndoableOperation {
	
	private final MyDocumentEvent event;
    private final String replacedText;
    
    public UndoableTextChange(DocumentEvent event, String replacedText) {
        this.event = new MyDocumentEvent(event);
        this.replacedText = replacedText;
    }
    
    private UndoableTextChange(MyDocumentEvent event, String replacedText) {
        this.event = event;
        this.replacedText = replacedText;
    }
    
    public void execute() {
        // nothing to do
    }
    
    public void undo() {
        //if (fDocumentUndoManager.fDocument instanceof IDocumentExtension4)
        //    ((IDocumentExtension4) fDocumentUndoManager.fDocument).replace(fStart, fText.length(),
        //        fPreservedText, fUndoModificationStamp);
        //else
        
        try {
            event.getDocument().replace(event.offset(), event.text().length(), replacedText);
        } catch (BadLocationException e) {
            //!
        }
    }
    
    public String getLabel() {
        return "some text changed: '" + replacedText + "' to '" + event.text() + "'";
    }

    public String getFactoryId() {
        // TODO Auto-generated method stub
        return null;
    }

    public void saveState(IMemento parent) {
        IMemento memento = parent.createChild("UndoableTextChange");
        
        memento.putString("replacedText", replacedText);
        memento.putMemento(MyMemento.forPersistableElement(event));
    }

    public static UndoableTextChange from(IMemento memento) {
        MyDocumentEvent event = new MyDocumentEvent(memento.getChild("MyDocumentEvent"));
        String replacedText = memento.getString("replacedText");
        
        return new UndoableTextChange(event, replacedText);
    }
}
