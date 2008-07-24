package com.yoursway.ide.worksheet.internal.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolStyledText;
import org.eclipse.swt.widgets.Display;

import com.mkalugin.swthell.CoolScrollBar;
import com.mkalugin.swthell.CoolScrollBarStyledTextBinding;
import com.yoursway.ide.worksheet.WorksheetStyle;
import com.yoursway.swt.animations.SizeAndAlphaAnimation;
import com.yoursway.swt.animations.SizeAndAlphaAnimationApplier;
import com.yoursway.swt.styledtext.extended.Inset;
import com.yoursway.swt.styledtext.extended.InsetSite;
import com.yoursway.swt.styledtext.extended.ResizeListener;
import com.yoursway.utils.annotations.DeadlockWarningBlocksOnUIThread;
import com.yoursway.utils.annotations.UseFromAnyThread;
import com.yoursway.utils.annotations.UseFromUIThread;

public class ResultInset implements Inset {
    
    private final WorksheetStyle style;
    private StyledText embeddedText;
    private CoolScrollBar scrollBar;
    private InsetSite site;
    
    private final SizeAndAlphaAnimation animation;
    private int alpha;
    
    private boolean pending;
    
    private int newLines = 0;
    
    @UseFromAnyThread
    public ResultInset(WorksheetStyle style) {
        if (style == null)
            throw new NullPointerException("settings is null");
        
        this.style = style;
        
        animation = new SizeAndAlphaAnimation();
    }
    
    @UseFromUIThread
    public void init(Composite composite, InsetSite site) {
        initSite(site);
        initComposite(composite);
        initEmbeddedText(composite);
        initAnimation(composite);
    }
    
    private void initSite(final InsetSite site) {
        if (site == null)
            throw new NullPointerException("site is null");
        
        this.site = site;
        
        site.addResizeListener(new ResizeListener() {
            @UseFromUIThread
            public void resized(Point size) {
                updateSize(true);
            }
        });
    }
    
    private void initComposite(final Composite composite) {
        composite.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                e.gc.setBackground(style.resultInsetColor());
                Point size = composite.getSize();
                int radius = 10, left = 10; //! magic
                e.gc.fillRoundRectangle(left, 0, size.x - left, size.y, radius, radius);
                
                e.gc.setBackground(site.getBackground());
                e.gc.setAlpha(255 - alpha);
                e.gc.fillRectangle(0, 0, size.x, size.y);
            }
        });
    }
    
    private void initEmbeddedText(Composite composite) {
        embeddedText = new CoolStyledText(composite, SWT.MULTI | SWT.WRAP);
        embeddedText.setFont(style.resultFont());
        embeddedText.setBackground(style.resultInsetColor());
        embeddedText.setForeground(style.outputColor());
        embeddedText.setEditable(false);
        embeddedText.setLocation(18, 5); //! magic
        
        scrollBar = new CoolScrollBar(composite, SWT.NO_BACKGROUND, true, style.resultScrollbarColor());
        scrollBar.setBeginMargin(2);
        scrollBar.setEndMargin(2);
        new CoolScrollBarStyledTextBinding(embeddedText, scrollBar, composite);
        
        scrollBar.setCursor(style.scrollbarCursor());
        
        ((CoolStyledText) embeddedText).setScrollBar(scrollBar);
        
        setText("", false);
        
        embeddedText.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                e.gc.setBackground(site.getBackground());
                e.gc.setAlpha(255 - alpha);
                Point size = embeddedText.getSize();
                e.gc.fillRectangle(0, 0, size.x, size.y);
            }
        });
    }
    
    private void initAnimation(final Composite composite) {
        alpha = 0;
        animation.targetAlpha(255);
        
        animation.start(new SizeAndAlphaAnimationApplier() {
            
            @UseFromAnyThread
            @DeadlockWarningBlocksOnUIThread
            public void updateSize(final int width, final int height) {
                if (composite.isDisposed())
                    return;
                
                composite.getDisplay().syncExec(new Runnable() {
                    public void run() {
                        if (composite.isDisposed())
                            return;
                        
                        composite.setSize(width, height);
                        
                        //! magic
                        scrollBar.setLocation(width - 12, 0);
                        scrollBar.setSize(12, height);
                    }
                });
            }
            
            @UseFromAnyThread
            @DeadlockWarningBlocksOnUIThread
            public void updateAlpha(final int alpha) {
                ResultInset.this.alpha = alpha;
                
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
            
            @UseFromAnyThread
            @DeadlockWarningBlocksOnUIThread
            public boolean visible() {
                if (composite.isDisposed())
                    return false;
                
                final boolean[] visible = new boolean[1];
                
                Display display = composite.getDisplay();
                if (display.isDisposed())
                    return false;
                display.syncExec(new Runnable() { //!
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
    
    @UseFromUIThread
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
    
    @UseFromAnyThread
    public boolean isDisposed() {
        //! (embeddedText == null) before init too 
        return embeddedText == null || embeddedText.isDisposed(); //?
    }
    
    @UseFromAnyThread
    @DeadlockWarningBlocksOnUIThread
    private void setText(final String text, boolean pending) {
        newLines = 0;
        becomeUpdated();
        this.pending = pending;
        
        embeddedText.getDisplay().syncExec(new Runnable() {
            public void run() {
                if (isDisposed())
                    return;
                
                embeddedText.setText(text);
                updateSize(false);
            }
        });
    }
    
    @UseFromAnyThread
    @DeadlockWarningBlocksOnUIThread
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
        
        embeddedText.getDisplay().syncExec(new Runnable() {
            public void run() {
                if (isDisposed())
                    return;
                
                int start = embeddedText.getCharCount();
                embeddedText.append(t);
                if (error) {
                    StyleRange style = ResultInset.this.style.errorStyle(start, t.length());
                    embeddedText.setStyleRange(style);
                }
                updateSize(false);
            }
        });
    }
    
    @UseFromUIThread
    private void updateSize(boolean siteResized) {
        if (isDisposed())
            return;
        
        if (pending && embeddedText.getSize().y > 0)
            return;
        
        Point size = embeddedText.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        
        int maxWidth = site.clientAreaSize().x - 50;
        if (maxWidth < 50)
            maxWidth = 50; //! hack, magic
            
        if (size.x > maxWidth)
            size = embeddedText.computeSize(maxWidth, SWT.DEFAULT);
        
        int maxHeight = site.clientAreaSize().y / 3; //! magic
        if (maxHeight > 100)
            maxHeight = 100; //! magic
            
        if (size.y > maxHeight)
            size.y = maxHeight;
        
        embeddedText.setSize(size);
        animation.targetSize(size.x + 35, size.y + 10); //! magic
        if (siteResized)
            animation.instantWidth();
    }
    
    @UseFromAnyThread
    public void becomeObsolete() {
        animation.targetAlpha(129);
    }
    
    @UseFromAnyThread
    private void becomeUpdated() {
        animation.targetAlpha(255);
    }
    
    @UseFromUIThread
    public void redraw() { //?
        if (isDisposed())
            return;
        
        embeddedText.redraw();
    }
    
    @UseFromAnyThread
    public void becomeWaiting() {
        setText("...", true);
    }
    
    @UseFromAnyThread
    public void reset() {
        setText("", true);
    }
    
}
