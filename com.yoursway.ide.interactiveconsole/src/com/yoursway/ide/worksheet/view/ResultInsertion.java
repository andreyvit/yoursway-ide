package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.yoursway.ide.worksheet.viewmodel.IUserSettings;

public class ResultInsertion implements Insertion {
    
    private final IUserSettings settings;
    private final ExtendedText extendedText;
    
    private final Animation animation;
    protected int alpha = 0;
    
    private Composite composite;
    private StyledText embeddedText;
    
    private boolean pending;
    private long whenUpdateSize = 0;
    
    private int newLines = 0;
    
    public ResultInsertion(IUserSettings settings, ExtendedText extendedText) {
        this.settings = settings;
        this.extendedText = extendedText;
        
        animation = new Animation();
    }
    
    public void createWidget(Composite parent) {
        composite = new Composite(parent, SWT.NO_FOCUS | SWT.NO_BACKGROUND);
        
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
        
        animation.targetAlpha(255);
        
        animation.start(new AnimationUpdater() {
            public void updateSize(int width, int height) {
                resizeComposite(width, height);
            }
            
            public void updateAlpha(final int alpha) {
                ResultInsertion.this.alpha = alpha;
                redraw();
            }
        });
    }
    
    public void dispose() {
        if (embeddedText != null && !embeddedText.isDisposed()) {
            embeddedText.dispose();
            embeddedText = null;
        }
        
        if (composite != null && !composite.isDisposed()) {
            composite.dispose();
            composite = null;
        }
    }
    
    public void updateLocation(PaintObjectEvent e) {
        Point size = composite.getSize();
        int x = e.x; //? + MARGIN;
        //? int y = e.y + e.ascent - size.y; //? - 2 * size.y / 3;
        int y = e.y + (e.ascent - size.y + e.descent) / 2 - 2; //! magic -2
        composite.setLocation(x, y);
    }
    
    private void setText(final String text, boolean pending) {
        newLines = 0;
        becomeUpdated();
        this.pending = pending;
        
        settings.display().syncExec(new Runnable() {
            public void run() {
                embeddedText.setText(text);
                updateSize();
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
        while (text.charAt(end - 1) == '\n') {
            end--;
            newLines++;
        }
        sb.append(text, 0, end);
        
        final String t = sb.toString();
        
        settings.display().syncExec(new Runnable() {
            public void run() {
                int start = embeddedText.getCharCount();
                embeddedText.append(t);
                if (error) {
                    StyleRange style = settings.errorStyle(start, t.length());
                    embeddedText.setStyleRange(style);
                }
                updateSize();
            }
        });
    }
    
    public void updateSize() {
        whenUpdateSize = 0;
        
        if (embeddedText.isDisposed())
            return;
        
        embeddedText.getDisplay().syncExec(new Runnable() {
            public void run() {
                if (pending && embeddedText.getSize().y > 0)
                    return;
                
                embeddedText.pack();
                
                int clientWidth = extendedText.getClientArea().width;
                int maxWidth = clientWidth - 50;
                
                if (embeddedText.getSize().x > maxWidth) {
                    //packed = false;
                    
                    embeddedText.setSize(maxWidth, embeddedText.getSize().y);
                    
                    if (embeddedText.getCharCount() > 0) {
                        Rectangle bounds = embeddedText.getTextBounds(0, embeddedText.getCharCount() - 1);
                        embeddedText.setSize(bounds.width, bounds.height);
                    }
                    
                    //> resizeComposite(embeddedText.getSize().x + 30, composite.getSize().y);
                }
                
                Point targetSize = embeddedText.getSize();
                animation.targetSize(targetSize.x + 30, targetSize.y + 10); //! magic
            }
        });
    }
    
    public void becomeObsolete() {
        animation.targetAlpha(129);
    }
    
    private void becomeUpdated() {
        animation.targetAlpha(255);
    }
    
    private void redraw() {
        if (composite.isDisposed())
            return;
        
        composite.getDisplay().asyncExec(new Runnable() {
            public void run() {
                if (composite.isDisposed() || embeddedText.isDisposed())
                    return;
                
                composite.redraw();
                embeddedText.redraw();
            }
        });
    }
    
    private void resizeComposite(final int width, final int height) {
        if (composite.isDisposed())
            return;
        
        composite.getDisplay().syncExec(new Runnable() {
            public void run() {
                if (composite.isDisposed())
                    return;
                
                composite.setSize(width, height);
                extendedText.updateMetrics(ResultInsertion.this, composite.getBounds());
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
