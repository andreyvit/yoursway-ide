package com.yoursway.ide.undo;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.FileEditorInputFactory;

public class MyDocumentEvent implements IPersistableElement {
    
    private FileEditorInput input;
    private int offset;
    private String text;

    public MyDocumentEvent(DocumentEvent event) {
        input = (FileEditorInput) DocumentsManager.get().input(event.getDocument());
        offset = event.fOffset;
        text = event.fText;
    }
    
    public MyDocumentEvent(IMemento memento) {
        IAdaptable adaptable = new FileEditorInputFactory().createElement(memento.getChild("input"));
        input = (FileEditorInput) adaptable.getAdapter(FileEditorInput.class);
        
        offset = memento.getInteger("offset");
        text = memento.getString("text");
    }

    public IDocument getDocument() {
        return DocumentsManager.get().document(input);
    }

    public int offset() {
        return offset;
    }

    public String text() {
        return text;
    }

    public String getFactoryId() {
        // TODO Auto-generated method stub
        return null;
    }

    public void saveState(IMemento parent) {
        IMemento memento = parent.createChild("MyDocumentEvent");
                
        memento.putInteger("offset", offset);
        memento.putString("text", text);
        
        memento.putMemento(MyMemento.childForPersistableElement("input", input));
    }

    public IEditorInput input() {
        return input;
    }

    public void setText(String text) {
        this.text = text;
    }
    
}
