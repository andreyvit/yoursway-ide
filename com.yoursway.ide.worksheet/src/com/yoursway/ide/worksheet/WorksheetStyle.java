package com.yoursway.ide.worksheet;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;

public interface WorksheetStyle {
    
    Font worksheetFont();
    
    Font resultFont();
    
    Color resultInsetColor();
    
    Color outputColor();
    
    StyleRange errorStyle(int offset, int length);
    
    Color resultScrollbarColor();
    
    Cursor scrollbarCursor();
    
}
