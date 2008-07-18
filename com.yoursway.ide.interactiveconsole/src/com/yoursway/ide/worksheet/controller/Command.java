package com.yoursway.ide.worksheet.controller;

import com.yoursway.ide.worksheet.view.ResultInsertion;

public class Command {
    
    private final String commandText;
    private final ResultInsertionGetter insertionGetter;
    
    public Command(String commandText, ResultInsertionGetter insertionGetter) {
        if (commandText.trim().length() == 0)
            throw new AssertionError("A command must not be empty.");
        
        this.commandText = commandText;
        this.insertionGetter = insertionGetter;
    }
    
    public String commandText() {
        return commandText;
    }
    
    public ResultInsertion insertion() {
        return insertionGetter.get();
    }
    
}
