package com.yoursway.ide.application.controllers.mainwindow;

import com.google.common.base.Function;
import com.yoursway.ide.application.controllers.EditorRegistry;
import com.yoursway.ide.application.model.Document;
import com.yoursway.ide.application.view.mainwindow.EditorWindow;
import com.yoursway.ide.application.view.mainwindow.EditorWindowCallback;
import com.yoursway.ide.application.view.mainwindow.EditorWindowFactory;
import com.yoursway.ide.editors.text.DocumentContentController;
import com.yoursway.ide.editors.text.DocumentContentWindow;
import com.yoursway.ide.editors.text.DocumentContentWindowCallback;
import com.yoursway.ide.editors.text.DocumentContentWindowFactory;
import com.yoursway.ide.editors.text.DocumentContentWindowImpl;
import com.yoursway.ide.editors.text.DocumentContentWindowModel;

public class DocumentController implements EditorWindowCallback {

    private EditorWindowModelImpl viewModel;
    private EditorWindow editor;
    private final Document document;

    public DocumentController(Document document, EditorWindowFactory factory, EditorRegistry editorRegistry) {
        if (document == null)
            throw new NullPointerException("document is null");
        this.document = document;
        viewModel = new EditorWindowModelImpl();
        editor = factory.createEditorWindow(viewModel, this);
        
        editorRegistry.createComponentFor(document, editor);
        viewModel.title.setValue(document.file().getName());
    }
    
    public Document document() {
        return document;
    }
    
    public static final Function<DocumentController, Document> GET_DOCUMENT = new Function<DocumentController, Document>() {

        public Document apply(DocumentController from) {
            return from.document();
        }
        
    };

    public void activateEditor() {
        editor.activateEditor();
    }
    
}
