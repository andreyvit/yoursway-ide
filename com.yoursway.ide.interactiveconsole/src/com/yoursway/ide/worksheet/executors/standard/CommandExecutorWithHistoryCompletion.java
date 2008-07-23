package com.yoursway.ide.worksheet.executors.standard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.yoursway.ide.oldconsole.CommandHistory;
import com.yoursway.ide.worksheet.executors.WorksheetCompletionProposal;
import com.yoursway.utils.annotations.Nullable;

public abstract class CommandExecutorWithHistoryCompletion extends AbstractWorksheetCommandExecutor {
    
    @Nullable
    private final CommandHistory history;
    
    public CommandExecutorWithHistoryCompletion(@Nullable CommandHistory history) {
        this.history = history;
    }
    
    public List<WorksheetCompletionProposal> complete(String command, int position) {
        String prefix = command.substring(0, position);
        
        Set<WorksheetCompletionProposal> proposals = new TreeSet<WorksheetCompletionProposal>();
        
        if (history != null) {
            String[] commands = history.getCommands();
            for (String item : commands) {
                if (item.startsWith(prefix)) {
                    WorksheetCompletionProposal o = new WorksheetCompletionProposal(0, command.length(), item);
                    proposals.add(o);
                }
            }
        }
        
        return new ArrayList<WorksheetCompletionProposal>(proposals);
    }
    
}