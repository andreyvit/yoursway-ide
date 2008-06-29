package com.yoursway.ide.editors.text;

import com.yoursway.ide.application.controllers.EditorComponentFactory;
import com.yoursway.ide.application.controllers.mainwindow.EditorComponent;
import com.yoursway.ide.application.model.Document;
import com.yoursway.ide.application.view.mainwindow.EditorWindow;

public class TextEditorComponentType implements EditorComponentFactory {

    public EditorComponent createFor(Document document, final EditorWindow editor) {
        return new DocumentContentController(document, new DocumentContentWindowFactory() {

            public DocumentContentWindow bind(DocumentContentWindowModel model,
                    DocumentContentWindowCallback callback) {
                return new DocumentContentWindowImpl(model, callback, editor);
            }
            
        });
    }

}
