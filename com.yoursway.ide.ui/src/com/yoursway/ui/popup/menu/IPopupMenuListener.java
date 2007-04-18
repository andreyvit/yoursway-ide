package com.yoursway.ui.popup.menu;

import org.eclipse.jface.action.IMenuManager;

/**
 * A party that gets a change in building a menu of the given popup each time
 * the menu is shown.
 * 
 * @author Andrey Tarantsov
 */
public interface IPopupMenuListener {
    
    void fillContextMenu(IMenuManager menuManager);
    
}
