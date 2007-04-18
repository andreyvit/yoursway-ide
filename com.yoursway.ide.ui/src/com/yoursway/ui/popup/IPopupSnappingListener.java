package com.yoursway.ui.popup;

/**
 * A listener that is being natified when the popup has been snapped to another
 * position on the screen.
 * 
 * @author Andrey Tarantsov
 */
public interface IPopupSnappingListener {
    
    void popupSnapped(SnappableYellowPopup popup, ISnapPosition newPosition);
    
}
