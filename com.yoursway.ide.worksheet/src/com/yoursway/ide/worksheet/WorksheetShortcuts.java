package com.yoursway.ide.worksheet;

import org.eclipse.swt.events.KeyEvent;

public interface WorksheetShortcuts {
    
    boolean isExecHotkey(KeyEvent e);
    
    boolean isRemoveInsertionsHotkey(KeyEvent e);
    
    boolean isShowTextHotkey(KeyEvent e);
    
}
