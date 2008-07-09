package com.yoursway.ide.interactiveconsole;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DebugMock implements IDebug {
    
    private final List<IDebugOutputListener> outputListeners = new LinkedList<IDebugOutputListener>();
    private final CommandHistory history;
    
    public DebugMock(CommandHistory history) {
        this.history = history;
        
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
    
    public List<CompletionProposal> complete(String command, int position) {
        String prefix = command.substring(0, position);
        
        Set<CompletionProposal> proposals = new TreeSet<CompletionProposal>();
        
        String[] commands = history.getCommands();
        for (String item : commands) {
            if (item.startsWith(prefix)) {
                CompletionProposal o = new CompletionProposal(0, command.length(), item);
                proposals.add(o);
            }
        }
        
        return new LinkedList<CompletionProposal>(proposals);
    }
}
