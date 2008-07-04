package com.yoursway.ide.undo;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

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
    
    @SuppressWarnings("restriction")
    public void undo() {
        //if (fDocumentUndoManager.fDocument instanceof IDocumentExtension4)
        //    ((IDocumentExtension4) fDocumentUndoManager.fDocument).replace(fStart, fText.length(),
        //        fPreservedText, fUndoModificationStamp);
        //else
        
        try {
            // open editor for document
            IWorkbenchWindow window = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow();
            //! if (window == null) return null;
            IWorkbenchPage p = window.getActivePage();
            //! if (p == null) throwPartInitException(JavaEditorMessages.EditorUtility_no_active_WorkbenchPage);
            p.openEditor(event.input(), "com.yoursway.ide.undo.txteditor", true);
            
            //initializeHighlightRange(editorPart);     - EditorUtility
            //return editorPart;          
            
            event.getDocument().replace(event.offset(), event.text().length(), replacedText);
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (PartInitException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
