package com.yoursway.ide.editors.text;

import org.eclipse.jface.internal.databinding.provisional.swt.ControlUpdater;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.yoursway.ide.application.view.mainwindow.EditorWindow;

public class DocumentContentWindowImpl implements DocumentContentWindow {

    private final DocumentContentWindowModel model;
    private final DocumentContentWindowCallback callback;
    private Composite composite;
    private SourceViewer sourceViewer;
    private Document document;

    public DocumentContentWindowImpl(DocumentContentWindowModel model,
            DocumentContentWindowCallback callback, EditorWindow editor) {
        if (model == null)
            throw new NullPointerException("model is null");
        if (callback == null)
            throw new NullPointerException("callback is null");
        if (editor == null)
            throw new NullPointerException("editor is null");
        this.model = model;
        this.callback = callback;
        composite = editor.composite();
        createWidgets();
        composite.getParent().layout();
    }

    private void createWidgets() {
        composite.setLayout(new FillLayout());
        sourceViewer = new SourceViewer(composite, null, null, false, SWT.NONE);
        document = new Document();
        sourceViewer.setDocument(document);
        new ControlUpdater(sourceViewer.getControl()) {
            protected void updateControl() {
                String data = model.data().getValue();
                document.set(data);
                composite.layout();
            }
        };
    }
    
}
