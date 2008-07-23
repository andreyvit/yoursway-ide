package com.yoursway.ide.worksheet.internal.controller;

import com.yoursway.ide.worksheet.executors.WorksheetCommandExecutor;
import com.yoursway.ide.worksheet.internal.view.ResultBlock;
import com.yoursway.utils.annotations.UseFromAnyThread;
import com.yoursway.utils.annotations.UseFromUIThread;

public class Execution {
    
    private final String command;
    private final ResultBlock output;
    private final WorksheetCommandExecutor executor;
    
    @UseFromUIThread
    public Execution(Command command, WorksheetCommandExecutor executor) {
        if (executor == null)
            throw new NullPointerException("executor is null");
        
        this.command = command.commandText();
        output = command.block();
        this.executor = executor;
        
        output.becomeWaiting();
    }
    
    @UseFromAnyThread
    public ResultBlock start() {
        output.reset();
        executor.executeCommand(command);
        return output;
    }
    
}
