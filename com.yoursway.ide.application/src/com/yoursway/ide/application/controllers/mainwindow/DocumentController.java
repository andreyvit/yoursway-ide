package com.yoursway.ide.application.controllers.mainwindow;

import com.yoursway.ide.application.model.Document;
import com.yoursway.ide.application.view.mainwindow.EditorWindow;
import com.yoursway.ide.application.view.mainwindow.EditorWindowCallback;
import com.yoursway.ide.application.view.mainwindow.EditorWindowFactory;

public class DocumentController implements EditorWindowCallback {

    private EditorWindowModelImpl viewModel;
    private EditorWindow editor;

    public DocumentController(Document document, EditorWindowFactory factory) {
        viewModel = new EditorWindowModelImpl();
        editor = factory.createEditorWindow(viewModel, this);
        viewModel.title.setValue(document.file().getName());
    }
    
}
