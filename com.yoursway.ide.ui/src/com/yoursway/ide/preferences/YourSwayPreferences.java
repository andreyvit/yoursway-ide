package com.yoursway.ide.preferences;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.yoursway.utils.EnvTest;

public class YourSwayPreferences {
    
    public static void show() {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        if (EnvTest.isMacOSX()) {
            PreferencesWindow dialog = new PreferencesWindow(shell);
            dialog.open();
        } else {
            PreferencesDialog dialog = new PreferencesDialog(new Shell());
            dialog.setBlockOnOpen(false);
            dialog.open();
        }
    }
    
    static final String WINDOW_TITLE = "YourSway IDE Preferences";
    
}
