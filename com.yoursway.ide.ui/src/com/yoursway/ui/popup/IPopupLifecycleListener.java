package com.yoursway.ui.popup;

/**
 * @author Andrey Tarantsov
 */
public interface IPopupLifecycleListener {
    
    /**
     * A popup has just been opened. The shell has been created and has been
     * made visible.
     */
    void popupOpened();
    
    /**
     * A popup has just been closed. The shell is either already disposed or
     * will be disposed after the broadcast of this event.
     */
    void popupClosed();
    
}
