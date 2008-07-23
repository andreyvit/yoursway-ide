package com.yoursway.ide.worksheet.internal.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import com.yoursway.ide.worksheet.WorksheetStyle;

public class StyleMock implements WorksheetStyle {
    
    private final Font font;
    private final Font resultFont;
    
    private final Color resultBackColor;
    private final Color outputColor;
    private final Color errorColor;
    
    public StyleMock(Display display) {
        font = new Font(display, "Monaco", 12, 0);
        resultFont = new Font(display, "Monaco", 12, SWT.BOLD);
        
        resultBackColor = new Color(display, 100, 100, 100);
        outputColor = new Color(display, 255, 255, 255);
        errorColor = new Color(display, 255, 192, 192);
        
    }
    
    public Font worksheetFont() {
        return font;
    }
    
    public Font resultFont() {
        return resultFont;
    }
    
    public Color resultBlockColor() {
        return resultBackColor;
    }
    
    public Color outputColor() {
        return outputColor;
    }
    
    public StyleRange errorStyle(int start, int length) {
        StyleRange style = new StyleRange();
        style.start = start;
        style.length = length;
        style.foreground = errorColor;
        return style;
    }
    
}
