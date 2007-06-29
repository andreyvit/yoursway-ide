package com.yoursway.rails.search.ui;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class RailsSearchControlWindow extends Window {
    
    public RailsSearchControlWindow(Shell parentShell) {
        super(parentShell);
    }
    
    @Override
    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
        return composite;
    }
    
}
