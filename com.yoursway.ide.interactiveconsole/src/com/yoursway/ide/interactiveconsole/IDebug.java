package com.yoursway.ide.interactiveconsole;

import java.util.List;

public interface IDebug {
    
    void addOutputListener(IDebugOutputListener listener);
    
    void executeCommand(String command);
    
    List<String> getHistory();
    
    void addToHistory(String newCommand);
    
    List<CompletionProposal> complete(String command, int position);
    
}
