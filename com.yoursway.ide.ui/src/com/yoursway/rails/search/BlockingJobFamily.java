/**
 * 
 */
package com.yoursway.rails.search;

public enum BlockingJobFamily {
    
    RUBY_SEARCH(true, 0.0),

    RAILS_SEARCH(true, 0.7),

    RAILS_INSTANCE_REGISTRATION(true, 0.3),

    USER_INTERFACE(false, 0.0);
    
    private final double relativeWeight;
    private final boolean showProgress;
    
    private BlockingJobFamily(boolean showProgress, double relativeWeight) {
        this.showProgress = showProgress;
        this.relativeWeight = relativeWeight;
    }
    
    public double getRelativeWeight() {
        return relativeWeight;
    }
    
    public boolean shouldShowProgress() {
        return showProgress;
    }
}