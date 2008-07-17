package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import com.yoursway.ide.worksheet.viewmodel.IUserSettings;

public class ResultInsertion implements Insertion {
    
    private final IUserSettings settings;
    private final ExtendedText extendedText;
    
    private StyledText embeddedText;
    
    private boolean obsolete;
    private boolean pending;
    private long whenUpdateSize = 0;
    
    public ResultInsertion(IUserSettings settings, ExtendedText extendedText) {
        this.settings = settings;
        this.extendedText = extendedText;
    }
    
    public void createWidget(Composite parent) {
        embeddedText = new StyledText(parent, SWT.MULTI | SWT.WRAP);
        embeddedText.setBackground(new Color(settings.display(), 220, 220, 220));
        embeddedText.setEditable(false);
        
        setText("");
        
        embeddedText.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                if (obsolete) {
                    e.gc.setBackground(extendedText.getBackground());
                    e.gc.setAlpha(129);
                    Point size = embeddedText.getSize();
                    e.gc.fillRectangle(0, 0, size.x, size.y);
                }
            }
        });
    }
    
    public void dispose() {
        if (embeddedText != null && !embeddedText.isDisposed()) {
            embeddedText.dispose();
            embeddedText = null;
        }
    }
    
    public void updateLocation(PaintObjectEvent e) {
        Point size = embeddedText.getSize();
        int x = e.x; //? + MARGIN;
        //? int y = e.y + e.ascent - size.y; //? - 2 * size.y / 3;
        int y = e.y + (e.ascent - size.y + e.descent) / 2;
        embeddedText.setLocation(x, y);
    }
    
    private void setText(final String text) {
        setText(text, false);
    }
    
    private void setText(final String text, boolean pending) {
        obsolete = false;
        this.pending = pending;
        
        settings.display().syncExec(new Runnable() {
            public void run() {
                embeddedText.setText(text);
                updateSizeNeatly();
            }
        });
    }
    
    public void append(final String text, final boolean error) {
        pending = false;
        
        settings.display().syncExec(new Runnable() {
            public void run() {
                int start = embeddedText.getCharCount();
                embeddedText.append(text);
                if (error) {
                    StyleRange style = settings.errorStyle(start, text.length());
                    embeddedText.setStyleRange(style);
                }
                updateSizeNeatly();
            }
        });
    }
    
    public void updateSize() {
        // instead of embeddedText.pack();
        
        if (pending) //?
            return;
        
        whenUpdateSize = 0;
        
        //! embeddedText can be disposed here (press COMMAND+Q)
        embeddedText.getDisplay().syncExec(new Runnable() {
            public void run() {
                int clientWidth = extendedText.getClientArea().width;
                embeddedText.setSize(clientWidth, embeddedText.getSize().y);
                
                if (embeddedText.getCharCount() > 0) {
                    Rectangle bounds = embeddedText.getTextBounds(0, embeddedText.getCharCount() - 1);
                    embeddedText.setSize(embeddedText.getSize().x, bounds.height);
                }
                
                extendedText.updateMetrics(ResultInsertion.this, embeddedText.getBounds());
            }
        });
    }
    
    private void updateSizeNeatly() {
        if (embeddedText.getCharCount() > 0) {
            Rectangle bounds = embeddedText.getTextBounds(0, embeddedText.getCharCount() - 1);
            int currentHeight = embeddedText.getSize().y;
            
            if (bounds.height > currentHeight)
                updateSize();
            
            if (bounds.height < currentHeight && !pending)
                updateSizeSometimes();
        }
    }
    
    private void updateSizeSometimes() {
        long now = System.currentTimeMillis();
        if (now >= whenUpdateSize) {
            if (whenUpdateSize == 0)
                whenUpdateSize = now + 300; //! magic
            else
                updateSize();
        }
    }
    
    public void becomeObsolete() {
        obsolete = true;
        embeddedText.redraw();
    }
    
    public void becomeWaiting() {
        setText("...", true);
    }
    
    public void reset() {
        setText("", true);
    }
    
}
