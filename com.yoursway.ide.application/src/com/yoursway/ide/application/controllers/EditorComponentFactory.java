package com.yoursway.ide.application.controllers;

import com.yoursway.ide.application.controllers.mainwindow.EditorComponent;
import com.yoursway.ide.application.model.Document;
import com.yoursway.ide.application.view.mainwindow.EditorWindow;

public interface EditorComponentFactory {
    
    EditorComponent createFor(Document document, EditorWindow editor);
    
}
