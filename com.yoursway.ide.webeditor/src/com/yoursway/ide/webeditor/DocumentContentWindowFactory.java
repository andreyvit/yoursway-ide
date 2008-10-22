package com.yoursway.ide.webeditor;

public interface DocumentContentWindowFactory {
    
    DocumentContentWindow bind(DocumentContentWindowModel model, DocumentContentWindowCallback callback);
    
}
