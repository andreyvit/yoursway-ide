package com.yoursway.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

public class MultiplexingProgressMonitor implements IProgressMonitor {
    
    private final List<IProgressMonitor> monitors = new ArrayList<IProgressMonitor>();
    
    public MultiplexingProgressMonitor addMonitor(IProgressMonitor monitor) {
        monitors.add(monitor);
        return this;
    }
    
    public void beginTask(String name, int totalWork) {
        for (IProgressMonitor monitor : monitors)
            monitor.beginTask(name, totalWork);
    }
    
    public void done() {
        for (IProgressMonitor monitor : monitors)
            monitor.done();
    }
    
    public void internalWorked(double work) {
        for (IProgressMonitor monitor : monitors)
            monitor.internalWorked(work);
    }
    
    public boolean isCanceled() {
        for (IProgressMonitor monitor : monitors)
            if (monitor.isCanceled())
                return true;
        return false;
    }
    
    public void setCanceled(boolean value) {
        for (IProgressMonitor monitor : monitors)
            monitor.setCanceled(value);
    }
    
    public void setTaskName(String name) {
        for (IProgressMonitor monitor : monitors)
            monitor.setTaskName(name);
    }
    
    public void subTask(String name) {
        for (IProgressMonitor monitor : monitors)
            monitor.subTask(name);
    }
    
    public void worked(int work) {
        for (IProgressMonitor monitor : monitors)
            monitor.worked(work);
    }
    
}
