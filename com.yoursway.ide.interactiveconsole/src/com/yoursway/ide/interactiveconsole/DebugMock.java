package com.yoursway.ide.interactiveconsole;

import java.util.LinkedList;
import java.util.List;

public class DebugMock implements IDebug {
    
    List<IDebugOutputListener> outputListeners = new LinkedList<IDebugOutputListener>();
    
    private final List<String> history = new LinkedList<String>();
    
    public DebugMock() {
        Thread outputter = new Thread() {
            
            @Override
            public void run() {
                try {
                    while (true) {
                        sleep(10000);
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
    
    public List<String> getHistory() {
        return history; //! returning changeable array field
    }
    
    public void addToHistory(String newCommand) {
        history.add(newCommand);
    }
    
    public List<CompletionProposal> complete(String command, int position) {
        String prefix = command.substring(0, position);
        
        List<CompletionProposal> proposals = new LinkedList<CompletionProposal>();
        
        for (String item : history) {
            if (item.startsWith(prefix))
                proposals.add(new CompletionProposal(0, command.length(), item));
        }
        
        return proposals;
    }
}
