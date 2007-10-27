package com.yoursway.ide.ui.railsview.shit;

import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TreeItem;

import com.yoursway.ide.ui.railsview.shit.SuperPuperMatcher.MatchResult;
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
    }
    
    public void paintItem(TreeItem item, Event event) {
        
        String pattern = infoProvider.getPattern();
        MatchResult match = null;
        if (pattern != null && pattern.length() > 0)
            match = SuperPuperMatcher.match(getCaption(), pattern); //XXX: possible speed-block
            
        GC gc = event.gc;
        TextLayout textLayout = new TextLayout(gc.getDevice());
        Font font = gc.getFont();
        Font bold = makeBold(gc.getDevice(), font);
        textLayout.setText(getCaption());
        textLayout.setFont(font);
        TextStyle style = new TextStyle(bold, gc.getForeground(), gc.getBackground());
        if (match != null) {
            for (IRegion region : match.regions) {
                textLayout.setStyle(style, region.getOffset(), region.getOffset() + region.getLength() - 1);
            }
        }
        textLayout.draw(gc, event.x, event.y, -1, -1, null, null, SWT.DRAW_TRANSPARENT);
        bold.dispose();
    }
    
    private Font makeBold(Device device, Font font) {
        FontData[] fontData = font.getFontData();
        fontData[0].style |= SWT.BOLD;
        return new Font(device, fontData);
    }
    
    public void eraseItem(TreeItem item, Event event) {
        event.detail &= ~SWT.FOREGROUND;
    }
    
}
