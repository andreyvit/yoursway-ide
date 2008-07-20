package com.yoursway.ide.editors.text;

import java.io.File;

import net.sf.colorer.ParserFactory;
import net.sf.colorer.eclipse.ColorerPlugin;
import net.sf.colorer.swt.ColorManager;
import net.sf.colorer.swt.TextColorer;

import org.eclipse.jface.internal.databinding.provisional.swt.ControlUpdater;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.yoursway.ide.application.view.mainwindow.EditorWindow;

public class DocumentContentWindowImpl implements DocumentContentWindow {

    private final DocumentContentWindowModel model;
    private final DocumentContentWindowCallback callback;
    private Composite composite;
//    private SourceViewer sourceViewer;
    private Document document;
    private StyledText text;
    private TextColorer textColorer;

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
        
        text = new StyledText (composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData spec = new GridData();
        spec.horizontalAlignment = GridData.FILL;
        spec.grabExcessHorizontalSpace = true;
        spec.verticalAlignment = GridData.FILL;
        spec.grabExcessVerticalSpace = true;
        text.setLayoutData(spec);

        text.setFont(new Font(composite.getDisplay(), "Monaco", 11, SWT.NORMAL));

        ParserFactory pf = ColorerPlugin.getDefault().getParserFactory();
        textColorer = new TextColorer(pf, new ColorManager());
        textColorer.attach(text);
        textColorer.setCross(true, true);
        textColorer.setRegionMapper("default", true);
        
//        text.addVer
        
        new ControlUpdater(text) {
            protected void updateControl() {
                File file = model.file().getValue();
                if (file == null)
                    throw new NullPointerException("file is null");
                String name = file.getName();
                if (name == null)
                    throw new NullPointerException("name is null");
                textColorer.chooseFileType(name);
                
                String data = model.data().getValue();
                text.setText(data);
                composite.layout();
            }
        };
    }
    
}
