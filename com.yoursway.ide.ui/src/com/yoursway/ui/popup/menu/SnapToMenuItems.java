package com.yoursway.ui.popup.menu;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;

import com.yoursway.ui.popup.SnapPosition;
import com.yoursway.ui.popup.SnappableYellowPopup;

/**
 * Adds popup menu items that can be used to switch between various snapping
 * positions for this popup.
 * 
 * @author Andrey Tarantsov
 */
public class SnapToMenuItems implements IPopupMenuListener {
    
    private static abstract class Item {
        
        public abstract void addTo(IMenuManager manager);
        
    }
    
    private class SnapItem extends Item {
        
        private final String description;
        private final SnapPosition snap;
        
        public SnapItem(SnapPosition snap, String description) {
            this.snap = snap;
            this.description = description;
        }
        
        public SnapPosition getSnap() {
            return snap;
        }
        
        public String getDescription() {
            return description;
        }
        
        @Override
        public void addTo(IMenuManager manager) {
            IAction action = new Action(getDescription(), IAction.AS_RADIO_BUTTON) {
                @Override
                public void run() {
                    popup.setSnapPosition(getSnap());
                    popup.activateParentShellIfRequested();
                }
            };
            action.setChecked(popup.getSnapPosition() == getSnap());
            manager.add(action);
        }
        
    }
    
    private static class SeparatorItem extends Item {
        
        @Override
        public void addTo(IMenuManager manager) {
            manager.add(new Separator());
        }
        
    }
    
    private final SnappableYellowPopup popup;
    private final List<Item> items = new ArrayList<Item>();
    
    public SnapToMenuItems(SnappableYellowPopup popup, MenuSupport menuSupport) {
        this.popup = popup;
        menuSupport.addMenuListener(this);
    }
    
    public void add(SnapPosition snap, String description) {
        items.add(new SnapItem(snap, description));
    }
    
    public void addSeparator() {
        items.add(new SeparatorItem());
    }
    
    public void fillContextMenu(IMenuManager menuManager) {
        for (Item item : items)
            item.addTo(menuManager);
    }
    
}
