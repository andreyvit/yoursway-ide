package com.yoursway.ide.application.view.mainwindow;

public interface EditorWindowFactory {
    
    EditorWindow createEditorWindow(EditorWindowModel model, EditorWindowCallback callback);
    
}
