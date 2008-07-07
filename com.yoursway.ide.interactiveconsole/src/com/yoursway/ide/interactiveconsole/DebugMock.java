package com.yoursway.ide.interactiveconsole;

import java.util.LinkedList;
import java.util.List;

public class DebugMock implements IDebug {
    
    List<IDebugOutputListener> outputListeners = new LinkedList<IDebugOutputListener>();
    
    public DebugMock() {
        Thread outputter = new Thread() {
            
            @Override
            public void run() {
                try {
                    while (true) {
                        sleep(2000);
                        outputString("hahaha\n");
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        };
        
        outputter.start();
    }
    
    public void addOutputListener(IDebugOutputListener listener) {
        outputListeners.add(listener);
    }
    
    public void executeCommand(String command) {
        outputString("ok: " + command + "\n");
    }
    
    private void outputString(String string) {
        for (IDebugOutputListener listener : outputListeners) {
            listener.outputString(string);
        }
    }
    
    public List<String> loadHistory() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void addToHistory(String newCommand) {
        // TODO Auto-generated method stub
        
    }
    
    public List<CompletionProposal> complete(String command, int position) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
