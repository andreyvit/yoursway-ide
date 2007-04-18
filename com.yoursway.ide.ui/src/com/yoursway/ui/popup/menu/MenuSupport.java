package com.yoursway.ui.popup.menu;

import org.eclipse.jface.action.IMenuListener2;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.services.IDisposable;

import com.yoursway.ide.images.YourSwayIDEImages;
import com.yoursway.ui.popup.IPopupLifecycleListener;
import com.yoursway.ui.popup.SnappableYellowPopup;
import com.yoursway.utils.TypedListenerList;

/**
 * Emulates an Eclipse-style view menu on the given popup. Contains a set of
 * methods that should be called during the building of the popup contents.
 * 
 * Delegates filling the menu with actions to the registered listeners.
 * 
 * @author Andrey Tarantsov
 */
public class MenuSupport implements IPopupLifecycleListener {
    
    public boolean isMenuCurrentlyShown = false;
    Image menuImage;
    
    private final TypedListenerList<IPopupMenuListener> menuListeners = new TypedListenerList<IPopupMenuListener>() {
        
        @Override
        protected IPopupMenuListener[] makeArray(int size) {
            return new IPopupMenuListener[size];
        }
        
    };
    
    public IDisposable addMenuListener(final IPopupMenuListener listener) {
        menuListeners.add(listener);
        return new IDisposable() {
            
            public void dispose() {
                menuListeners.remove(listener);
            }
            
        };
    }
    
    public MenuSupport(SnappableYellowPopup popup) {
        popup.addLifecycleListener(this);
    }
    
    private void showMenu(Composite table, ToolBar toolBar, MenuManager menuManager) {
        Menu menu = menuManager.createContextMenu(toolBar);
        menu.setLocation(toolBar.toDisplay(0, toolBar.getSize().y));
        isMenuCurrentlyShown = true;
        menu.setVisible(true);
    }
    
    /**
     * Creates a menu manager that will manage the newly created view menu. The
     * manager returned by this method should be disposed with the popup by the
     * caller.
     * 
     * It is possible to create several menu managers for a single popup, to
     * support several menus added e.g. to different pages in a pagebook. All of
     * such menu managers are to be disposed.
     */
    public MenuManager createMenuManager() {
        MenuManager menuManager = new MenuManager();
        menuManager.setRemoveAllWhenShown(true);
        menuManager.addMenuListener(new IMenuListener2() {
            public void menuAboutToHide(IMenuManager manager) {
                isMenuCurrentlyShown = false;
            }
            
            public void menuAboutToShow(IMenuManager manager) {
                addMenuItems(manager);
            }
        });
        return menuManager;
    }
    
    private void addMenuItems(IMenuManager manager) {
        IPopupMenuListener[] listeners = menuListeners.getListeners();
        for (IPopupMenuListener listener : listeners)
            listener.fillContextMenu(manager);
    }
    
    /**
     * Creates an Eclipse-style view menu trigger button as a child of the given
     * composite. Uses the given menu manager (that should have been created by
     * a call to <code>createMenuManager</code>) to display the menu.
     */
    public ToolBar addViewMenu(final Composite parent, final MenuManager menuManager) {
        final ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
        final ToolItem menuButton = new ToolItem(toolBar, SWT.PUSH, 0);
        if (menuImage == null)
            menuImage = YourSwayIDEImages.DESC_ELCL_VIEW_MENU.createImage();
        menuButton.setImage(menuImage);
        menuButton.setToolTipText("Menu");
        toolBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                showMenu(parent, toolBar, menuManager);
            }
        });
        menuButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                showMenu(parent, toolBar, menuManager);
            }
        });
        toolBar.pack();
        return toolBar;
    }
    
    public void popupClosed() {
        if (menuImage != null) {
            menuImage.dispose();
            menuImage = null;
        }
    }
    
    public void popupOpened() {
    }
    
    /**
     * Returns whether the menu is currently visible.
     */
    public boolean isMenuCurrentlyShown() {
        return isMenuCurrentlyShown;
    }
    
}
