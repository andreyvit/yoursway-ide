package com.yoursway.ide.worksheet;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.yoursway.ide.worksheet.executors.WorksheetCommandExecutor;

public interface UserSettings {
    
    WorksheetCommandExecutor executor();
    
    Display display();
    
    String worksheetTitle();
    
    Rectangle worksheetBounds();
    
    Font workspaceFont();
    
    boolean isExecHotkey(KeyEvent e);
    
    boolean isRemoveInsertionsHotkey(KeyEvent e);
    
    boolean isShowTextHotkey(KeyEvent e);
    
    StyleRange errorStyle(int offset, int length);
    
    String insertionPlaceholder();
    
}
