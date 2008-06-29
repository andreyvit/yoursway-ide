package com.yoursway.ide.application.controllers;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import com.yoursway.ide.application.controllers.mainwindow.EditorComponent;
import com.yoursway.ide.application.model.Document;
import com.yoursway.ide.application.view.mainwindow.EditorWindow;
import com.yoursway.ide.application.view.mainwindow.EditorWindowFactory;

public class EditorRegistry {
    
    private Collection<EditorComponentFactory> types = newArrayList();
    
    public void add(EditorComponentFactory type) {
        if (type == null)
            throw new NullPointerException("type is null");
        types.add(type);
    }
    
    public EditorComponent createComponentFor(Document document, EditorWindow editor) {
        for (EditorComponentFactory type : types) {
            EditorComponent result = type.createFor(document, editor);
            if (result != null)
                return result;
        }
        return null;
    }
    
}
