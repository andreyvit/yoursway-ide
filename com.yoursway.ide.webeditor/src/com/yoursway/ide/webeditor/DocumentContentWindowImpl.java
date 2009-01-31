package com.yoursway.ide.webeditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jface.internal.databinding.provisional.swt.ControlUpdater;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.xml.sax.helpers.ParserFactory;

import com.yoursway.ide.application.model.Document;
import com.yoursway.ide.application.view.mainwindow.EditorWindow;
import com.yoursway.web.editing.BrowserAdditions;
import com.yoursway.web.editing.django.wrapper.ParserRunner;

public class DocumentContentWindowImpl implements DocumentContentWindow {

    private final DocumentContentWindowModel model;
    private final DocumentContentWindowCallback callback;
    private Composite composite;
//    private SourceViewer sourceViewer;
    private Document document;
	private BrowserAdditions ba;

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
//        sourceViewer = new SourceViewer(composite, null, null, false, SWT.NONE);
//        document = new Document();
//        sourceViewer.setDocument(document);
        
        ba = new BrowserAdditions(composite, new ParserRunner("/tmp/django"));
        ba.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        new ControlUpdater(ba.getControl()) {
            protected void updateControl() {
                File file = model.file().getValue();
                if (file == null)
                    throw new NullPointerException("file is null");
                
                String data = model.data().getValue();
                ba.setHtml(data);
                composite.layout();
            }
        };
    }
    
}
