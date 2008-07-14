package com.yoursway.ide.worksheet.viewmodel;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public interface IUserSettings {
    
    Display display();
    
    String worksheetTitle();
    
    Rectangle worksheetBounds();
    
    Font workspaceFont();
    
    boolean isExecHotkey(KeyEvent e);
    
    boolean isRemoveInsertionsHotkey(KeyEvent e);
    
}
