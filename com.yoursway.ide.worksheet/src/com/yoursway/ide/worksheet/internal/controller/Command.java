package com.yoursway.ide.worksheet.internal.controller;

import com.yoursway.ide.worksheet.internal.view.ResultBlock;
import com.yoursway.utils.annotations.UseFromUIThread;

public class Command {
    
    private final String commandText;
    private final ResultBlockProvider blockProvider;
    
    public Command(String commandText, ResultBlockProvider blockProvider) {
        if (commandText.trim().length() == 0)
            throw new AssertionError("A command must not be empty.");
        if (blockProvider == null)
            throw new NullPointerException("blockProvider is null");
        
        this.commandText = commandText;
        this.blockProvider = blockProvider;
    }
    
    public String commandText() {
        return commandText;
    }
    
    @UseFromUIThread
    public ResultBlock block() {
        return blockProvider.get();
    }
    
}
