package com.yoursway.ide.debug.model;

import java.util.LinkedList;
import java.util.List;


public abstract class AbstractDebug implements IDebug {
    
    private final List<ITerminationListener> terminationListeners = new LinkedList<ITerminationListener>();
    private final List<IOutputListener> outputListeners = new LinkedList<IOutputListener>();
    private final List<OutputEntry> output = new LinkedList<OutputEntry>();
    
    public void addTerminationListener(ITerminationListener listener) {
        terminationListeners.add(listener);
    }
    
    protected void terminated() {
        for (ITerminationListener listener : terminationListeners) {
            listener.terminated();
        }
    }
    
    public synchronized void addOutputListener(IOutputListener listener) {
        for (OutputEntry e : output) {
            listener.outputted(e.text(), e.error());
        }
        
        outputListeners.add(listener);
    }
    
    protected void output(String text) {
        output(text, false);
    }
    
    protected synchronized void output(String text, boolean error) {
        output.add(new OutputEntry(text, error));
        
        for (IOutputListener listener : outputListeners) {
            listener.outputted(text, error);
        }
    }
    
    protected IOutputListener outputter() {
        return new IOutputListener() {
            
            public void outputted(String text, boolean error) {
                output(text, error);
            }
            
        };
    }
    
}