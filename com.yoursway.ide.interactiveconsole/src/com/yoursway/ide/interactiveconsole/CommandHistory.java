package com.yoursway.ide.interactiveconsole;

import java.util.List;

public class CommandHistory {
    
    public String[] commands;
    public int index;
    
    private final IDebug debug;
    
    public CommandHistory(IDebug debug) {
        this.debug = debug; //? move history responsibility from IDebug
        
        //> load old persistent history
        
        update();
    }
    
    public void add(String command) {
        debug.addToHistory(command);
        update();
    }
    
    public void update() {
        List<String> h = debug.getHistory();
        commands = h.toArray(new String[h.size() + 1]);
        commands[h.size()] = "";
        index = h.size();
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
}