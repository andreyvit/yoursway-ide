package com.yoursway.ide.application.controllers.mainwindow;

import static com.yoursway.utils.YsFileUtils.readAsString;

import java.io.IOException;

import com.yoursway.ide.application.model.Document;

public class DocumentContentController implements DocumentContentWindowCallback {

    private DocumentContentWindow view;
    private final Document document;
    private DocumentContentWindowModelImpl viewModel;

    public DocumentContentController(Document document, DocumentContentWindowFactory factory) {
        if (document == null)
            throw new NullPointerException("document is null");
        this.document = document;
        this.viewModel = new DocumentContentWindowModelImpl();
        this.view = factory.bind(this.viewModel, this);
        try {
            viewModel.data.setValue(readAsString(document.file()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
