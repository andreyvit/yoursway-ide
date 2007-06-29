package com.yoursway.utils;

public interface PercentageProgressListener {
    
    public void started();
    
    public void percentageUpdated(double percentDone);
    
    public void completed(boolean canceled);
    
}
