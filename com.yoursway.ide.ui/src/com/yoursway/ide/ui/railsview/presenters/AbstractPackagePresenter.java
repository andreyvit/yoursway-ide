package com.yoursway.ide.ui.railsview.presenters;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TreeItem;

import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.ide.ui.railsview.presentation.AbstractPresenter;
import com.yoursway.ide.ui.railsview.presentation.IContextMenuContext;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.ide.ui.railsview.presentation.IProvidesTreeItem;

public abstract class AbstractPackagePresenter extends AbstractPresenter {
    
    final int ITEM_COUNT = 8;
    final int TEXT_MARGIN = 3;

    public AbstractPackagePresenter(IPresenterOwner owner) {
        super(owner);
    }

    public boolean canEditInPlace() {
        return false;
    }

    public void fillContextMenu(IContextMenuContext context) {
    }

    public Object[] getChildren() {
        Collection<Object> children = new ArrayList<Object>();
        return children.toArray();
    }

    public ImageDescriptor getImage() {
        return RailsViewImages.PARTIAL_ICON;
    }

    public Object getParent() {
        return null;
    }

    public void handleDoubleClick(IProvidesTreeItem context) {
    }

    public boolean hasChildren() {
        return false;
    }

    @Override
    public void measureItem(TreeItem item, Object element, Event event) {
        Rectangle bounds = item.getBounds();
        event.height = bounds.height;
        event.width = bounds.width + 10;
        String text = getCaption();
        Point size = event.gc.textExtent(text);
        Rectangle clientArea = getOwner().getTree().getClientArea();
        event.width = clientArea.width - 20; // size.x + 2 * TEXT_MARGIN;
        event.height = Math.max(event.height, size.y + TEXT_MARGIN);
        event.height = 20;
    }

    @Override
    public void eraseItem(TreeItem item, Object element, Event event) {
        event.detail &= ~SWT.FOREGROUND;
    }

    @Override
    public void paintItem(TreeItem item, Object element, Event event) {
        String text = item.getText(event.index);
        int yOffset = 0;
        Point size = event.gc.textExtent(text);
        yOffset = Math.max(0, (event.height - size.y) / 2) - 1;
        Rectangle clientArea = getOwner().getTree().getClientArea();
        event.gc.setBackground(new Color(item.getDisplay(), 0x00, 0x00, 0x00));
        event.gc.setForeground(new Color(item.getDisplay(), 0x00, 0x00, 0x00));
        event.gc.fillRoundRectangle(5, event.y, clientArea.width - 5, event.height - 2, 10, 10);
        //        event.gc.setForeground(new Color(item.getDisplay(), 0x00, 0x00, 0x00));
        //        event.gc.setBackground(new Color(item.getDisplay(), 0xFF, 0xFF, 0xFF));
        //        event.gc.fillGradientRectangle(0, event.y + event.height / 2, clientArea.width, event.height / 2,
        //                true);
        event.gc.setForeground(new Color(item.getDisplay(), 0xFF, 0xFF, 0xFF));
        event.gc.drawText(text, event.x + TEXT_MARGIN, event.y + yOffset, true);
    }
    
}
