package com.yoursway.ide.ui.railsview.presenters;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
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
	public void eraseItem(TreeItem item, Object element, Event event) {
		event.detail &= ~SWT.FOREGROUND;
	}

	private void expandRegion(Event event, Scrollable scrollable, GC gc,
			Rectangle area) {
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

	@Override
	public void paintItem(TreeItem item, Object element, Event event) {
		// Color categoryGradientStart = new Color(Display.getDefault(), 245,
		// 245,
		// 250);
		Color categoryGradientEnd = new Color(Display.getDefault(), 205, 205,
				205);

		Scrollable scrollable = (Scrollable) event.widget;
		GC gc = event.gc;

		Rectangle area = scrollable.getClientArea();
		Rectangle rect = event.getBounds();

		/* Paint the selection beyond the end of last column */
		expandRegion(event, scrollable, gc, area);
		/* Draw Gradient Rectangle */
		Color oldForeground = gc.getForeground();
		Color oldBackground = gc.getBackground();

		// gc.setForeground(categoryGradientEnd);
		// gc.drawLine(0, rect.y, area.width, rect.y);

		// gc.setForeground(categoryGradientStart);
		gc.setBackground(categoryGradientEnd);

		// gc.setForeground(categoryGradientStart);
		// gc.setBackground(categoryGradientEnd);
		// gc.setForeground(new Color(Display.getCurrent(), 255, 0, 0));

		// gc.fillGradientRectangle(0, rect.y + 1, area.width,
		// rect.height, true);
		//				
		gc.fillRoundRectangle(3, rect.y + 1 + 1, area.width, rect.height - 2,
				10, 10);
		// gc.fillRoundRectangle(5, 5, 100, 20, 10, 10);

		/* Bottom Line */
		// gc.setForeground();
		gc.setForeground(categoryGradientEnd);
		// gc.drawLine(0, rect.y + rect.height - 1, area.width, rect.y
		// + rect.height - 1);

		gc.setForeground(oldForeground);
		gc.setBackground(oldBackground);
		/* Mark as Background being handled */
		event.detail &= ~SWT.BACKGROUND;

		String text = item.getText();
		event.gc.drawText(text, event.x + TEXT_MARGIN, event.y + 2, true);

	}

}
