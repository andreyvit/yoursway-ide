package com.yoursway.ide.ui.railsview.presenters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class TextEditorPopup {
    
    private final Composite control;
    private ControlEditor treeEditor;
    private final Text text;
    
    public TextEditorPopup(Composite parentControl) {
        control = new Composite(parentControl, SWT.NONE);
        GridLayout shellLayout = new GridLayout();
        shellLayout.marginWidth = 1;
        shellLayout.marginHeight = 1;
        control.setLayout(shellLayout);
        control.setBackground(parentControl.getForeground());
        
        text = new Text(control, SWT.NONE);
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    }
    
    public void open(ControlEditor controlEditor) {
        this.treeEditor = controlEditor;
        
        final Point minSize = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        controlEditor.minimumWidth = Math.max(100, minSize.x);
        controlEditor.minimumHeight = minSize.y;
        controlEditor.setEditor(control);
        control.setFocus();
    }
    
    public Composite getControl() {
        return control;
    }
    
    public Text getTextControl() {
        return text;
    }
    
    public void dispose() {
        if (control != null && !control.isDisposed())
            control.dispose();
        treeEditor.setEditor(null);
    }
}
