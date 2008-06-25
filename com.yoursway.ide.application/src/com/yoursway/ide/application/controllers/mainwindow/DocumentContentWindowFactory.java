package com.yoursway.ide.application.controllers.mainwindow;

public interface DocumentContentWindowFactory {
    
    DocumentContentWindow bind(DocumentContentWindowModel model, DocumentContentWindowCallback callback);
    
}
