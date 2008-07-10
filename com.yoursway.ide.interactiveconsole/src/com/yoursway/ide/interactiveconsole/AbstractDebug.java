package com.yoursway.ide.interactiveconsole;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractDebug implements IDebug {
    
    private final List<IDebugOutputListener> outputListeners = new LinkedList<IDebugOutputListener>();
    private final StringBuilder output = new StringBuilder();
    
    public void addOutputListener(IDebugOutputListener listener) {
        outputListeners.add(listener);
        
        listener.outputString(output.toString());
    }
    
    protected void outputString(String string) {
        output.append(string);
        
        for (IDebugOutputListener listener : outputListeners) {
            listener.outputString(string);
        }
    }
    
}