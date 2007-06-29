package com.yoursway.rails.search.ui;


public class RailsSearchingView {
    
    private final Callback callback;
    private RailsSearchingProgressWindow progressWindow;
    
    public interface Callback {
        
    }
    
    public RailsSearchingView(Callback callback) {
        this.callback = callback;
    }
    
    public synchronized void showSearchProgress() {
        if (progressWindow == null) {
            progressWindow = new RailsSearchingProgressWindow(null);
            progressWindow.open();
        }
    }
    
    public synchronized void showManualSelection() {
        
    }
    
    public synchronized void hideSearchProgress() {
        
    }
    
    public synchronized boolean isInBlockingMode() {
        return false;
    }
    
}
