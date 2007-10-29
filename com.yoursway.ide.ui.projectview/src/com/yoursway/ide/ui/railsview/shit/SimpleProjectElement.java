package com.yoursway.ide.ui.railsview.shit;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TreeItem;

import com.yoursway.model.repository.IResolver;

public abstract class SimpleProjectElement extends ProjectElement {
    
    private String name;
    protected final IViewInfoProvider infoProvider;
    private final IPresentableItem parent;
    
    public SimpleProjectElement(IPresentableItem parent, String name, IViewInfoProvider infoProvider) {
        this.parent = parent;
        this.name = name;
        this.infoProvider = infoProvider;
    }
    
    public SimpleProjectElement(IPresentableItem parent, IViewInfoProvider infoProvider) {
        this.parent = parent;
        this.infoProvider = infoProvider;
    }
    
    public String getCaption() {
        return getName();
    }
    
    public String getName() {
        return name;
    }
    
    protected IResolver getResolver() {
        return infoProvider.getModelResolver();
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int matches(String pattern) {
        MatchResult match = SuperPuperMatcher.match(getCaption(), pattern);
        if (match == null)
            return -1;
        return match.kind;
    }
    
    @Override
    public IPresentableItem getParent() {
        return parent;
    }
    
    public void measureItem(TreeItem item, Event event) {
        TextLayout textLayout = createTextLayout(event.gc);
        
        TextLayout textLayout2 = createPlainTextLayout(event.gc);
        event.width += (textLayout.getBounds().width - textLayout2.getBounds().width);
    }
    
    public void paintItem(TreeItem item, Event event) {
        GC gc = event.gc;
        Rectangle textBounds = item.getTextBounds(0);
        TextLayout textLayout = createTextLayout(gc);
        Image image = getImage();
        if (image != null)
            gc.drawImage(image, item.getImageBounds(0).x, item.getImageBounds(0).y);
        textLayout.draw(gc, textBounds.x, textBounds.y + 1, -1, -1, null, null, SWT.DRAW_TRANSPARENT);
    }
    
    private TextLayout createTextLayout(GC gc) {
        String pattern = infoProvider.getPattern();
        MatchResult match = null;
        if (pattern != null && pattern.length() > 0)
            match = SuperPuperMatcher.match(getCaption(), pattern); //XXX: possible speed-block
            
        TextLayout textLayout = new TextLayout(gc.getDevice());
        Font font = gc.getFont();
        Font boldFont = new LocalResourceManager(JFaceResources.getResources()).createFont(FontDescriptor
                .createFrom(font).setStyle(SWT.BOLD));
        textLayout.setFont(font);
        textLayout.setText(getCaption());
        TextStyle style = new TextStyle(boldFont, gc.getForeground(), gc.getBackground());
        if (match != null) {
            for (IRegion region : match.regions) {
                textLayout.setStyle(style, region.getOffset(), region.getOffset() + region.getLength() - 1);
            }
        }
        return textLayout;
    }
    
    private TextLayout createPlainTextLayout(GC gc) {
        TextLayout textLayout = new TextLayout(gc.getDevice());
        Font font = gc.getFont();
        textLayout.setFont(font);
        textLayout.setText(getCaption());
        return textLayout;
    }
    
    public void eraseItem(TreeItem item, Event event) {
        event.detail &= ~SWT.FOREGROUND;
    }
    
}
