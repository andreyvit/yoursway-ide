package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.yoursway.ide.worksheet.viewmodel.IUserSettings;

public class ResultInsertion implements InsertionContent {
    
    private final IUserSettings settings;
    private final ExtendedText extendedText;
    
    private final Animation animation;
    private int alpha;
    
    private StyledText embeddedText;
    
    private boolean pending;
    
    private int newLines = 0;
    
    public ResultInsertion(IUserSettings settings, ExtendedText extendedText) {
        this.settings = settings;
        this.extendedText = extendedText;
        
        animation = new Animation();
    }
    
    public void init(final Composite composite, ListenersAcceptor la) {
        
        la.addResizeListener(new ResizeListener() {
            public void resized(Point size) {
                updateSize(true);
            }
        });
        
        Display display = composite.getDisplay();
        final Color color = new Color(display, 100, 100, 100); //! magic
        
        composite.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                e.gc.setBackground(color);
                Point size = composite.getSize();
                int radius = 10, left = 10; //! magic
                e.gc.fillRoundRectangle(left, 0, size.x - left, size.y, radius, radius);
                
                e.gc.setBackground(extendedText.getBackground());
                e.gc.setAlpha(255 - alpha);
                e.gc.fillRectangle(0, 0, size.x, size.y);
            }
        });
        
        embeddedText = new StyledText(composite, SWT.MULTI | SWT.WRAP);
        embeddedText.setBackground(color);
        embeddedText.setForeground(new Color(display, 255, 255, 255)); //! magic
        embeddedText.setFont(new Font(display, "Monaco", 12, SWT.BOLD)); //! magic
        embeddedText.setEditable(false);
        embeddedText.setLocation(18, 5); //! magic
        
        setText("", false);
        
        embeddedText.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                e.gc.setBackground(extendedText.getBackground());
                e.gc.setAlpha(255 - alpha);
                Point size = ((Control) e.widget).getSize();
                e.gc.fillRectangle(0, 0, size.x, size.y);
            }
        });
        
        alpha = 0;
        animation.targetAlpha(255);
        
        animation.start(new AnimationUpdater() {
            public void updateSize(final int width, final int height) {
                if (composite.isDisposed())
                    return;
                
                composite.getDisplay().syncExec(new Runnable() {
                    public void run() {
                        if (composite.isDisposed())
                            return;
                        
                        composite.setSize(width, height);
                    }
                });
            }
            
            public void updateAlpha(final int alpha) {
                ResultInsertion.this.alpha = alpha;
                
                if (composite.isDisposed())
                    return;
                
                composite.getDisplay().syncExec(new Runnable() {
                    public void run() {
                        if (composite.isDisposed())
                            return;
                        
                        composite.redraw();
                        redraw();
                    }
                });
            }
            
            public boolean visible() {
                if (composite.isDisposed())
                    return false;
                
                final boolean[] visible = new boolean[1];
                composite.getDisplay().syncExec(new Runnable() {
                    public void run() {
                        if (composite.isDisposed()) {
                            visible[0] = false;
                            return;
                        }
                        
                        visible[0] = composite.getVisible();
                    }
                });
                
                return visible[0];
            }
        });
    }
    
    public void dispose() {
        if (!animation.isDisposed()) {
            animation.dispose();
        }
        
        if (embeddedText != null) {
            if (!embeddedText.isDisposed())
                embeddedText.dispose();
            embeddedText = null;
        }
    }
    
    public boolean isDisposed() {
        //! (embeddedText == null) before init too 
        return embeddedText == null || embeddedText.isDisposed();
    }
    
    private void setText(final String text, boolean pending) {
        newLines = 0;
        becomeUpdated();
        this.pending = pending;
        
        settings.display().syncExec(new Runnable() {
            public void run() {
                if (isDisposed())
                    return;
                
                embeddedText.setText(text);
                updateSize(false);
            }
        });
    }
    
    public void append(String text, final boolean error) {
        pending = false;
        
        StringBuilder sb = new StringBuilder();
        
        for (; newLines > 0; newLines--) {
            sb.append('\n');
        }
        
        int end = text.length();
        while (end > 0 && text.charAt(end - 1) == '\n') {
            end--;
            newLines++;
        }
        sb.append(text, 0, end);
        
        final String t = sb.toString();
        
        settings.display().syncExec(new Runnable() {
            public void run() {
                if (isDisposed())
                    return;
                
                int start = embeddedText.getCharCount();
                embeddedText.append(t);
                if (error) {
                    StyleRange style = settings.errorStyle(start, t.length());
                    embeddedText.setStyleRange(style);
                }
                updateSize(false);
            }
        });
    }
    
    private void updateSize(boolean containerResized) {
        if (isDisposed())
            return;
        
        if (pending && embeddedText.getSize().y > 0)
            return;
        
        Point size = embeddedText.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        
        int maxWidth = extendedText.getClientArea().width - 50;
        if (maxWidth < 10)
            maxWidth = 10; //! hack
            
        if (size.x > maxWidth)
            size = embeddedText.computeSize(maxWidth, SWT.DEFAULT);
        
        embeddedText.setSize(size);
        animation.targetSize(size.x + 30, size.y + 10); //! magic
        if (containerResized)
            animation.instantWidth();
    }
    
    public void becomeObsolete() {
        animation.targetAlpha(129);
    }
    
    private void becomeUpdated() {
        animation.targetAlpha(255);
    }
    
    public void redraw() { //?
        if (isDisposed())
            return;
        
        embeddedText.getDisplay().asyncExec(new Runnable() {
            public void run() {
                if (isDisposed())
                    return;
                
                embeddedText.redraw();
            }
        });
    }
    
    public void becomeWaiting() {
        setText("...", true);
    }
    
    public void reset() {
        setText("", true);
    }
    
}
