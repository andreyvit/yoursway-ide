package com.yoursway.ide.interactiveconsole;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public abstract class DebugWithHistoryCompletion extends AbstractDebug {
    
    private final CommandHistory history;
    
    public DebugWithHistoryCompletion(CommandHistory history) {
        this.history = history;
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