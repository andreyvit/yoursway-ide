package com.yoursway.ide.application.model;

import java.io.File;

public class Document {

    private final DocumentOwner owner;
    private final File file;

    public Document(DocumentOwner owner, File file) {
        if (owner == null)
            throw new NullPointerException("owner is null");
        if (file == null)
            throw new NullPointerException("file is null");
        this.owner = owner;
        this.file = file;
    }
    
    public File file() {
        return file;
    }
    
}
