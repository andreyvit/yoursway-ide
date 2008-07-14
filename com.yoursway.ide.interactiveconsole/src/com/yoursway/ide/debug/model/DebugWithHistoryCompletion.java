package com.yoursway.ide.debug.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.yoursway.ide.interactiveconsole.CommandHistory;

public abstract class DebugWithHistoryCompletion extends AbstractDebug {
    
    private final CommandHistory history;
    
    public DebugWithHistoryCompletion(CommandHistory history) {
        this.history = history; // can be null
    }
    
    public List<CompletionProposal> complete(String command, int position) {
        String prefix = command.substring(0, position);
        
        Set<CompletionProposal> proposals = new TreeSet<CompletionProposal>();
        
        if (history != null) {
            String[] commands = history.getCommands();
            for (String item : commands) {
                if (item.startsWith(prefix)) {
                    CompletionProposal o = new CompletionProposal(0, command.length(), item);
                    proposals.add(o);
                }
            }
        }
        
        return new ArrayList<CompletionProposal>(proposals);
    }
    
}