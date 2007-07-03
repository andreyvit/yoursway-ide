package com.yoursway.ide.preferences;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class PreferencesWindow extends Window {
    
    protected PreferencesWindow(Shell parentShell) {
        super(parentShell);
    }
    
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(YourSwayPreferences.WINDOW_TITLE);
    }
    
    @Override
    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 10;
        layout.marginWidth = 10;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        PreferencesComposite prefs = new PreferencesComposite(composite);
        prefs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        return composite;
    }
    
}
