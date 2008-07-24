package com.yoursway.ide.worksheet.internal.controller;

import com.yoursway.ide.worksheet.internal.view.ResultInset;
import com.yoursway.utils.annotations.UseFromUIThread;

public class Command {
    
    private final String commandText;
    private final ResultInsetProvider insetProvider;
    
    public Command(String commandText, ResultInsetProvider insetProvider) {
        if (commandText.trim().length() == 0)
            throw new AssertionError("A command must not be empty.");
        if (insetProvider == null)
            throw new NullPointerException("insetProvider is null");
        
        this.commandText = commandText;
        this.insetProvider = insetProvider;
    }
    
    public String commandText() {
        return commandText;
    }
    
    @UseFromUIThread
    public ResultInset inset() {
        return insetProvider.get();
    }
    
}
