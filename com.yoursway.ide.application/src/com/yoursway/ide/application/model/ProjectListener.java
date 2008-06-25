package com.yoursway.ide.application.model;

public interface ProjectListener {

    void documentAdded(Document document, DocumentAdditionReason reason);

    void documentAlreadyOpen(Document document);
    
}
