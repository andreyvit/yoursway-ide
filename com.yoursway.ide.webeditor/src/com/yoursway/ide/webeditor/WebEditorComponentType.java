package com.yoursway.ide.webeditor;

import com.yoursway.ide.application.controllers.EditorComponentFactory;
import com.yoursway.ide.application.controllers.mainwindow.EditorComponent;
import com.yoursway.ide.application.model.Document;
import com.yoursway.ide.application.view.mainwindow.EditorWindow;

public class WebEditorComponentType implements EditorComponentFactory {

	public EditorComponent createFor(Document document, final EditorWindow editor) {
		if (!document.file().getName().toLowerCase().endsWith(".html"))
			return null;
        return new DocumentContentController(document, new DocumentContentWindowFactory() {

            public DocumentContentWindow bind(DocumentContentWindowModel model,
                    DocumentContentWindowCallback callback) {
                return new DocumentContentWindowImpl(model, callback, editor);
            }
            
        });
	}

}
