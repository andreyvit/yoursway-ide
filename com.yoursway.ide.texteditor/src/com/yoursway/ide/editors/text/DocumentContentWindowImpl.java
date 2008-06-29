package com.yoursway.ide.editors.text;

import org.eclipse.jface.internal.databinding.provisional.swt.ControlUpdater;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.yoursway.ide.application.view.mainwindow.EditorWindow;

public class DocumentContentWindowImpl implements DocumentContentWindow {

    private final DocumentContentWindowModel model;
    private final DocumentContentWindowCallback callback;
    private Composite composite;
    private Text text;

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
        text = new Text(composite, SWT.MULTI);
        new ControlUpdater(text) {
            protected void updateControl() {
                text.setText(model.data().getValue());
                text.redraw();
                composite.layout();
            }
        };
    }
    
}
