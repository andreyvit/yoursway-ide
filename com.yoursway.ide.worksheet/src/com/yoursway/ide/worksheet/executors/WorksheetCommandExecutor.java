package com.yoursway.ide.worksheet.executors;

import java.util.List;

public interface WorksheetCommandExecutor {
    
    void addTerminationListener(TerminationListener listener);
    
    void addOutputListener(OutputListener listener);
    
    void executeCommand(String command);
    
    List<WorksheetCompletionProposal> complete(String command, int position);
    
}
