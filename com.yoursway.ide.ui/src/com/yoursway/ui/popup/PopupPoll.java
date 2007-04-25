package com.yoursway.ui.popup;

/**
 * The poll object is used to track a single voting round among the parties
 * willing to contribute to the visibility of the popup control.
 * 
 * If no votes are received during the round, the popup object is made visible.
 * If some votes are received, the most limiting vote is chosen and "applied".
 * 
 * Note that <code>voteClose</code> is always the strongest vote. It should be
 * preferred over explicit call to <code>close()</code> method because it
 * allows the popup to synchronize closing correctly.
 * 
 * Implementers of derived popups may subclass <code>PopupPoll</code> to
 * provide more polling possibilities.
 * 
 * @author Andrey Tarantsov
 */
public class PopupPoll {
    
    private enum Result {
        
        ACTIVE,

        INACTIVE,

        HIDDEN,

        CLOSE
        
    }
    
    private Result strongestResult = Result.ACTIVE;
    private final SnappableYellowPopup popup;
    
    public PopupPoll(SnappableYellowPopup popup) {
        this.popup = popup;
    }
    
    private void vote(Result result) {
        if (result.compareTo(strongestResult) > 0)
            strongestResult = result;
    }
    
    public void voteClose() {
        vote(Result.CLOSE);
    }
    
    public void voteInactive() {
        vote(Result.INACTIVE);
    }
    
    public void voteHidden() {
        vote(Result.HIDDEN);
    }
    
    public void publishResultsInNewspaper() {
        if (popup.getShell() == null || popup.getShell().isDisposed())
            return;
        if (strongestResult == Result.CLOSE) {
            popup.close();
        } else if (strongestResult == Result.HIDDEN) {
            popup.setVisible(false);
        } else if (strongestResult == Result.INACTIVE || strongestResult == Result.ACTIVE) {
            popup.setVisible(true);
        } else {
            throw new AssertionError("Impossible state");
        }
    }
}
