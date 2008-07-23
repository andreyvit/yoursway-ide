package com.yoursway.ide.worksheet.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.yoursway.ide.worksheet.UserSettings;
import com.yoursway.ide.worksheet.executors.WorksheetCommandExecutor;
import com.yoursway.ide.worksheet.executors.standard.ExternalCommandExecutor;

public class UserSettingsMock implements UserSettings {
    
    private final WorksheetCommandExecutor executor;
    private final Display display;
    
    public UserSettingsMock() {
        executor = new ExternalCommandExecutor("irb");
        display = new Display();
    }
    
    public WorksheetCommandExecutor executor() {
        return executor;
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
    
    public boolean isShowTextHotkey(KeyEvent e) {
        return e.stateMask == SWT.COMMAND && e.character == 's';
    }
    
    public StyleRange errorStyle(int start, int length) {
        StyleRange style = new StyleRange();
        style.start = start;
        style.length = length;
        style.foreground = new Color(display, 255, 192, 192);
        return style;
    }
    
    public String insertionPlaceholder() {
        return "\uFFFC";
    }
    
}
