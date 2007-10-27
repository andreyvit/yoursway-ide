package com.yoursway.ide.ui.railsview.shit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public abstract class ElementsCategory implements IPresentableItem {
    
    final int TEXT_MARGIN = 3;
    
    protected final IViewInfoProvider infoProvider;
    private final String name;
    
    public int getPriority() {
        return 0;
    }
    
    public ElementsCategory(String name, IViewInfoProvider searchProvider) {
        this.name = name;
        this.infoProvider = searchProvider;
    }
    
    public String getCaption() {
        return name;
    }
    
    public Image getImage() {
        return null;
    }
    
    public int matches(String pattern) {
        return name.indexOf(pattern);
    }
    
    public boolean headerOnly() {
        return false;
    }
    
    public void measureItem(TreeItem item, Event event) {
    }
    
    public void eraseItem(TreeItem item, Event event) {
        event.detail &= ~SWT.FOREGROUND;
    }
    
    private void expandRegion(Event event, Scrollable scrollable, GC gc, Rectangle area) {
        int columnCount;
        if (scrollable instanceof Table)
            columnCount = ((Table) scrollable).getColumnCount();
        else
            columnCount = ((Tree) scrollable).getColumnCount();
        
        if (event.index == columnCount - 1 || columnCount == 0) {
            int width = area.x + area.width - event.x;
            if (width > 0) {
                Region region = new Region();
                gc.getClipping(region);
                region.add(event.x, event.y, width, event.height);
                gc.setClipping(region);
                region.dispose();
            }
        }
    }
    
    public void paintItem(TreeItem item, Event event) {
        Color categoryGradientEnd = new Color(Display.getDefault(), 205, 205, 205);
        
        Scrollable scrollable = (Scrollable) event.widget;
        GC gc = event.gc;
        
        Rectangle area = scrollable.getClientArea();
        Rectangle rect = event.getBounds();
        
        /* Paint the selection beyond the end of last column */
        expandRegion(event, scrollable, gc, area);
        /* Draw Gradient Rectangle */
        Color oldForeground = gc.getForeground();
        Color oldBackground = gc.getBackground();
        
        gc.setBackground(categoryGradientEnd);
        
        gc.fillRoundRectangle(3, rect.y, area.width, rect.height - 1, 10, 10);
        gc.setForeground(categoryGradientEnd);
        
        gc.setForeground(oldForeground);
        gc.setBackground(oldBackground);
        /* Mark as Background being handled */
        event.detail &= ~SWT.BACKGROUND;
        
        Font font = gc.getFont();
        TextLayout text = new TextLayout(gc.getDevice());
        text.setFont(font);
        text.setText(name);
        text.draw(gc, event.x + TEXT_MARGIN, event.y + 2, -1, -1, null, null, SWT.DRAW_TRANSPARENT);
        
        if (drawFocus(event)) {
            gc.drawFocus(3, rect.y, area.width, rect.height - 1);
        }
    }
    
    private boolean drawFocus(Event event) {
        return (event.detail & SWT.SELECTED) != 0;
    }
    
}
