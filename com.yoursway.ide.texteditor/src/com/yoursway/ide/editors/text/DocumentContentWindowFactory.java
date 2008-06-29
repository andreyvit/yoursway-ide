package com.yoursway.ide.editors.text;

public interface DocumentContentWindowFactory {
    
    DocumentContentWindow bind(DocumentContentWindowModel model, DocumentContentWindowCallback callback);
    
}
