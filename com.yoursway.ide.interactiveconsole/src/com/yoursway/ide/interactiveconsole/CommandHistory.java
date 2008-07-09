package com.yoursway.ide.interactiveconsole;

import java.util.LinkedList;
import java.util.List;

public class CommandHistory {
    
    private final List<String> history = new LinkedList<String>();
    
    // working history
    private String[] commands;
    private int index;
    
    public CommandHistory() {
        //> load old persistent history
        prepareWorkingHistory();
    }
    
    public void add(String command) {
        history.add(command);
        prepareWorkingHistory();
    }
    
    private void prepareWorkingHistory() {
        int size = history.size();
        commands = history.toArray(new String[size + 1]);
        commands[size] = "";
        index = size;
    }
    
    public void next(String currentCommand) {
        commands[index] = currentCommand;
        
        if (index < commands.length - 1)
            index++;
    }
    
    public void previous(String currentCommand) {
        commands[index] = currentCommand;
        
        if (index > 0)
            index--;
    }
    
    public String command() {
        return commands[index];
    }
    
    //! special for DebugMock
    public String[] getCommands() {
        return history.toArray(new String[0]);
    }
    
}