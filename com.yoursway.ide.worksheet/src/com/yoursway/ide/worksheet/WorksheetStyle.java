package com.yoursway.ide.worksheet;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Font;

public interface WorksheetStyle {
    
    Font worksheetFont();
    
    StyleRange errorStyle(int offset, int length);
    
}
