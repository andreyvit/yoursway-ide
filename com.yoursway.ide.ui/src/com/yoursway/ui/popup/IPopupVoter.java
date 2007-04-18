package com.yoursway.ui.popup;

/**
 * A callback interface which should be implemented by parties wishing to
 * participate in determining popup visibility and closing conditions.
 * 
 * @author Andrey Tarantsov
 */
public interface IPopupVoter {
    
    /**
     * The given poll object should be notified if the voter wants to vote for
     * making the popup invisible or for closing it.
     * 
     * The poll object passed might be a derivative of <code>PopupPoll</code>,
     * provided by custom popups that wish to provide more polling
     * possibilities.
     */
    void participate(PopupPoll poll);
    
}
