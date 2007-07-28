package com.yoursway.utils;

import org.eclipse.core.runtime.IProgressMonitor;

import com.yoursway.common.TypedListenerList;

public class PercentageBroadcastingProgressMonitor implements IProgressMonitor {
    
    private boolean done;
    private boolean canceled;
    private boolean started;
    
    private int totalWork = IProgressMonitor.UNKNOWN;
    
    private double currWork;
    
    private final TypedListenerList<PercentageProgressListener> listeners = new TypedListenerList<PercentageProgressListener>();
    
    public PercentageBroadcastingProgressMonitor withListener(PercentageProgressListener listener) {
        addListener(listener);
        return this;
    }
    
    public void addListener(PercentageProgressListener listener) {
        listeners.add(listener);
    }
    
    public void beginTask(String name, int totalWork) {
        this.totalWork = totalWork;
        started();
    }
    
    public void done() {
        currWork = totalWork;
        this.done = true;
        started();
        for (PercentageProgressListener listener : listeners)
            listener.completed(canceled);
    }
    
    public int getPercentage() {
        if (done) {
            return 100;
        }
        if (totalWork == IProgressMonitor.UNKNOWN)
            return 0;
        if (currWork >= totalWork)
            return 100;
        return (int) (100 * currWork / totalWork);
    }
    
    public void internalWorked(double work) {
        currWork += work;
        if (currWork > totalWork)
            currWork = totalWork;
        else if (currWork < 0)
            currWork = 0;
        for (PercentageProgressListener listener : listeners)
            listener.percentageUpdated(getPercentage());
    }
    
    public boolean isCanceled() {
        return canceled;
    }
    
    public boolean isDone() {
        return done;
    }
    
    public void removeListener(PercentageProgressListener listener) {
        listeners.remove(listener);
    }
    
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
    
    public void setTaskName(String name) {
    }
    
    public void subTask(String name) {
    }
    
    public void worked(int work) {
        internalWorked(work);
    }
    
    private void started() {
        if (started)
            return;
        started = true;
        for (PercentageProgressListener listener : listeners)
            listener.started();
    }
    
}
