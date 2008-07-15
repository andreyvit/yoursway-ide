package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.yoursway.ide.worksheet.viewmodel.IUserSettings;

public class Insertion {
    
    private int offset;
    private final StyledText embeddedStyledText;
    private final Worksheet worksheet;
    private final IUserSettings settings;
    
    public Insertion(final int offset, String text, final Worksheet worksheet, IUserSettings settings) {
        this.settings = settings;
        
        this.offset = offset;
        this.worksheet = worksheet;
        
        embeddedStyledText = new StyledText(worksheet.styledText(), SWT.MULTI | SWT.WRAP);
        embeddedStyledText.setBackground(new Color(settings.display(), 220, 220, 220));
        embeddedStyledText.setEditable(false);
        
        setText(text);
    }
    
    public int offset() {
        return offset;
    }
    
    public void dispose() {
        if (disposed())
            return;
        if (embeddedStyledText != null && !embeddedStyledText.isDisposed()) {
            embeddedStyledText.dispose();
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
            Point size = embeddedStyledText.getSize();
            int x = e.x; //? + MARGIN;
            //? int y = e.y + e.ascent - size.y; //? - 2 * size.y / 3;
            int y = e.y + (e.ascent - size.y + e.descent) / 2;
            embeddedStyledText.setLocation(x, y);
        }
    }
    
    public void setText(final String text) {
        settings.display().syncExec(new Runnable() {
            public void run() {
                //! check isDisposed
                embeddedStyledText.setText(text);
                updateSize();
            }
        });
    }
    
    public void append(String text, boolean error) {
        int start = embeddedStyledText.getCharCount();
        embeddedStyledText.append(text);
        if (error) {
            StyleRange style = settings.errorStyle(start, text.length());
            embeddedStyledText.setStyleRange(style);
        }
        updateSize();
    }
    
    public void updateSize() {
        // instead of embeddedStyledText.pack();
        
        int clientWidth = worksheet.styledText().getClientArea().width;
        embeddedStyledText.setSize(clientWidth, embeddedStyledText.getSize().y);
        
        if (embeddedStyledText.getCharCount() > 0) {
            Rectangle bounds = embeddedStyledText.getTextBounds(0, embeddedStyledText.getCharCount() - 1);
            embeddedStyledText.setSize(embeddedStyledText.getSize().x, bounds.height);
        }
        
        worksheet.updateMetrics(offset, embeddedStyledText);
    }
}
