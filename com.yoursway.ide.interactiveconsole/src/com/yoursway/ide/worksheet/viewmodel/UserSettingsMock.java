package com.yoursway.ide.worksheet.viewmodel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public class UserSettingsMock implements IUserSettings {
    
    private final Display display;
    
    public UserSettingsMock() {
        display = new Display();
    }
    
    public Display display() {
        return display;
    }
    
    public Rectangle worksheetBounds() {
        return new Rectangle(240, 120, 640, 480);
    }
    
    public String worksheetTitle() {
        return "Worksheet";
    }
    
    public Font workspaceFont() {
        return new Font(display, "Monaco", 12, 0);
    }
    
    public boolean isExecHotkey(KeyEvent e) {
        return e.stateMask == SWT.COMMAND && e.character == 'e';
    }
    
    public boolean isRemoveInsertionsHotkey(KeyEvent e) {
        return e.stateMask == SWT.COMMAND && e.character == 'r';
    }
    
}
