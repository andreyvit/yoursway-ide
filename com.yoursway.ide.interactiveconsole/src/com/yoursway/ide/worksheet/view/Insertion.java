package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;

public class Insertion {
    
    private int offset;
    private final Label label;
    private final Worksheet worksheet;
    
    public Insertion(int offset, Label label, Worksheet worksheet) {
        this.offset = offset;
        this.label = label;
        
        this.worksheet = worksheet;
    }
    
    public int offset() {
        return offset;
    }
    
    public void dispose() {
        if (disposed())
            return;
        if (label != null && !label.isDisposed()) {
            label.dispose();
            //? label = null;
        }
        offset = -1;
    }
    
    public boolean disposed() {
        return offset == -1;
    }
    
    public void updateOffset(VerifyEvent e) {
        int start = e.start;
        int replaceCharCount = e.end - e.start;
        int newCharCount = e.text.length();
        
        if (start <= offset && offset < start + replaceCharCount)
            dispose();
        
        if (!disposed() && offset >= start)
            offset += newCharCount - replaceCharCount;
    }
    
    public void updateLocation(PaintObjectEvent e) {
        StyleRange style = e.style;
        int start = style.start;
        if (start == offset) {
            Point size = label.getSize();
            int x = e.x; //? + MARGIN;
            int y = e.y + e.ascent - size.y; //? - 2 * size.y / 3;
            label.setLocation(x, y);
        }
    }
    
    public void setText(String text) {
        label.setText(text);
        
        worksheet.updateMetrics(offset, label);
    }
}
