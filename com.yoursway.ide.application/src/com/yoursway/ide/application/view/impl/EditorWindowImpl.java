package com.yoursway.ide.application.view.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

import com.yoursway.databinding.ControlUpdater;
import com.yoursway.ide.application.view.mainwindow.EditorWindow;
import com.yoursway.ide.application.view.mainwindow.EditorWindowCallback;
import com.yoursway.ide.application.view.mainwindow.EditorWindowModel;

public class EditorWindowImpl implements EditorWindow {

    private final EditorWindowCallback callback;
    private final EditorWindowModel model;
    private CTabItem item;
    private Composite composite;

    public EditorWindowImpl(EditorWindowModel model, EditorWindowCallback callback, CTabFolder tabFolder) {
        if (model == null)
            throw new NullPointerException("model is null");
        if (callback == null)
            throw new NullPointerException("callback is null");
        if (tabFolder == null)
            throw new NullPointerException("tabFolder is null");
        
        this.model = model;
        this.callback = callback;
        item = new CTabItem(tabFolder, SWT.NONE);
        composite = new Composite(tabFolder, SWT.NONE);
        item.setControl(composite);
        update(tabFolder);
        tabFolder.setSelection(item);
    }

    private void update(CTabFolder tabFolder) {
        new ControlUpdater(tabFolder) {
            protected void updateControl() {
                item.setText(model.title().getValue());
            }
        };
    }

    public Composite composite() {
        return composite;
    }
    
}
