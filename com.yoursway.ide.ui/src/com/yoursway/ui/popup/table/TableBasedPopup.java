package com.yoursway.ui.popup.table;

import java.util.Collection;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.part.PageBook;

import com.yoursway.ui.popup.IPopupHost;
import com.yoursway.ui.popup.SnapPosition;
import com.yoursway.ui.popup.SnappableYellowPopup;
import com.yoursway.ui.popup.menu.MenuSupport;
import com.yoursway.utils.SWTUtils;

/**
 * A popup that uses a particular 2-column grid layout and provides
 * <code>InfoEntry</code> class to add hyperlink/keybinding rows easily.
 * 
 * @author Andrey Tarantsov
 */
public abstract class TableBasedPopup extends SnappableYellowPopup {
    
    private MenuManager menuManager;
    private final MenuSupport menuSupport;
    
    public TableBasedPopup(IPopupHost popupOwner, Collection<? extends SnapPosition> snapPositions,
            ParentShellActivation activateParentShell) {
        super(popupOwner, snapPositions, activateParentShell);
        menuSupport = new MenuSupport(this);
    }
    
    public MenuSupport getMenuSupport() {
        return menuSupport;
    }
    
    @Override
    protected void dispose() {
        if (menuManager != null)
            menuManager.dispose();
        super.dispose();
    }
    
    protected abstract void createContent(final Display display, Color foreground, final Composite table);
    
    @Override
    protected Control createPopupContents(Composite parent) {
        final Display display = parent.getDisplay();
        Color foreground = display.getSystemColor(SWT.COLOR_INFO_FOREGROUND);
        Color background = display.getSystemColor(SWT.COLOR_INFO_BACKGROUND);
        
        final PageBook book = new PageBook(parent, SWT.NONE);
        final Composite table = new Composite(book, SWT.NONE);
        
        GridLayout tableLayout = new GridLayout(2, false);
        tableLayout.marginHeight = 4;
        tableLayout.marginWidth = 4;
        tableLayout.horizontalSpacing = 10;
        tableLayout.verticalSpacing = 2;
        table.setLayout(tableLayout);
        
        createContent(display, foreground, table);
        
        addMoveSupport(table);
        
        menuManager = menuSupport.createMenuManager();
        ToolBar tableToolBar = menuSupport.addViewMenu(table, menuManager);
        GridData gridData = new GridData();
        gridData.exclude = true;
        tableToolBar.setLayoutData(gridData);
        Point tableSize = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        // having the button overlap the keybinding for refactor is OK, since Ctrl+Enter is always wider than Enter...
        // TODO: this is NOT okay for us!
        tableToolBar.setLocation(tableSize.x - tableToolBar.getSize().x, 0);
        
        SWTUtils.recursiveSetBackgroundColor(book, background);
        book.showPage(table);
        return table;
    }
    
}
