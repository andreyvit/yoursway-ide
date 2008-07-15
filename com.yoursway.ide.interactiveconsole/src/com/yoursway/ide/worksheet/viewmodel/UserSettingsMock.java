package com.yoursway.ide.worksheet.viewmodel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.yoursway.ide.debug.model.ExternalDebug;
import com.yoursway.ide.debug.model.IDebug;

public class UserSettingsMock implements IUserSettings {
    
    private final IDebug debug;
    private final Display display;
    
    public UserSettingsMock() {
        debug = new ExternalDebug("irb", null);
        display = new Display();
    }
    
    public IDebug debug() {
        return debug;
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
        return e.stateMask == SWT.COMMAND && (e.character == '\n' || e.character == '\r');
    }
    
    public boolean isRemoveInsertionsHotkey(KeyEvent e) {
        return e.stateMask == SWT.COMMAND && e.character == 'r';
    }
    
    public StyleRange errorStyle(int start, int length) {
        StyleRange style = new StyleRange();
        style.start = start;
        style.length = length;
        style.foreground = new Color(display, 192, 0, 0);
        return style;
    }
    
}
