package com.yoursway.ide.interactiveconsole;

import java.util.List;

public interface IDebug {
    
    void addOutputListener(IOutputListener listener);
    
    void executeCommand(String command);
    
    List<CompletionProposal> complete(String command, int position);
    
}
