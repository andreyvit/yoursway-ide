package com.yoursway.ide.worksheet.internal.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import com.yoursway.ide.worksheet.WorksheetShortcuts;

public class ShortcutsMock implements WorksheetShortcuts {
    
    public boolean isExecHotkey(KeyEvent e) {
        return e.stateMask == SWT.COMMAND && (e.character == '\n' || e.character == '\r');
    }
    
    public boolean isRemoveInsetsHotkey(KeyEvent e) {
        return e.stateMask == SWT.COMMAND && e.character == 'r';
    }
    
    public boolean isShowTextHotkey(KeyEvent e) {
        return e.stateMask == SWT.COMMAND && e.character == 's';
    }
    
}
