package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import com.yoursway.ide.worksheet.viewmodel.IUserSettings;

public class Insertion {
    
    private int offset;
    private final StyledText embeddedText;
    private final Worksheet worksheet;
    private final IUserSettings settings;
    private final Composite parent;
    private boolean obsolete;
    
    public Insertion(final int offset, String text, final Worksheet worksheet, final Composite parent,
            IUserSettings settings) {
        this.settings = settings;
        
        this.offset = offset;
        this.worksheet = worksheet;
        this.parent = parent;
        
        embeddedText = new StyledText(parent, SWT.MULTI | SWT.WRAP);
        embeddedText.setBackground(new Color(settings.display(), 220, 220, 220));
        embeddedText.setEditable(false);
        
        setText(text);
        
        embeddedText.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                if (obsolete) {
                    e.gc.setBackground(parent.getBackground());
                    e.gc.setAlpha(129);
                    Point size = embeddedText.getSize();
                    e.gc.fillRectangle(0, 0, size.x, size.y);
                }
            }
        });
        
    }
    
    public int offset() {
        return offset;
    }
    
    public void dispose() {
        if (disposed())
            return;
        if (embeddedText != null && !embeddedText.isDisposed()) {
            embeddedText.dispose();
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
            Point size = embeddedText.getSize();
            int x = e.x; //? + MARGIN;
            //? int y = e.y + e.ascent - size.y; //? - 2 * size.y / 3;
            int y = e.y + (e.ascent - size.y + e.descent) / 2;
            embeddedText.setLocation(x, y);
        }
    }
    
    private void setText(final String text) {
        obsolete = false;
        
        settings.display().syncExec(new Runnable() {
            public void run() {
                //! check isDisposed
                embeddedText.setText(text);
                updateSize();
            }
        });
    }
    
    public void append(final String text, final boolean error) {
        settings.display().syncExec(new Runnable() {
            public void run() {
                //! check isDisposed
                int start = embeddedText.getCharCount();
                embeddedText.append(text);
                if (error) {
                    StyleRange style = settings.errorStyle(start, text.length());
                    embeddedText.setStyleRange(style);
                }
                updateSize();
            }
        });
    }
    
    public void updateSize() {
        // instead of embeddedStyledText.pack();
        
        int clientWidth = parent.getClientArea().width;
        embeddedText.setSize(clientWidth, embeddedText.getSize().y);
        
        if (embeddedText.getCharCount() > 0) {
            Rectangle bounds = embeddedText.getTextBounds(0, embeddedText.getCharCount() - 1);
            embeddedText.setSize(embeddedText.getSize().x, bounds.height);
        }
        
        worksheet.updateMetrics(offset, embeddedText.getBounds());
    }
    
    public void becomeObsolete() {
        obsolete = true;
        embeddedText.redraw();
    }
    
    public void becomeWaiting() {
        setText("...");
    }
    
    public void reset() {
        setText("");
    }
}
