package com.yoursway.ide.worksheet.viewmodel;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.yoursway.ide.debug.model.IDebug;

public interface IUserSettings {
    
    IDebug debug();
    
    Display display();
    
    String worksheetTitle();
    
    Rectangle worksheetBounds();
    
    Font workspaceFont();
    
    boolean isExecHotkey(KeyEvent e);
    
    boolean isRemoveInsertionsHotkey(KeyEvent e);
    
}
