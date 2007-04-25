package com.yoursway.ide.ui.railsview.presenters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class TreeItemOverlayEditor {
    
    //    private Shell popup;
    
    private Composite composite;
    
    public void createContent(Composite baseControl) {
        composite = new Composite(baseControl, SWT.NONE);
        GridLayout shellLayout = new GridLayout();
        shellLayout.marginWidth = 1;
        shellLayout.marginHeight = 1;
        composite.setLayout(shellLayout);
        composite.setBackground(baseControl.getForeground());
        createEditor(composite);
    }
    
    private void createEditor(Composite composite) {
        
    }
    
}
