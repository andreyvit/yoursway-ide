package com.yoursway.ide.worksheet.internal.controller;

import com.yoursway.ide.worksheet.executors.WorksheetCommandExecutor;
import com.yoursway.ide.worksheet.internal.view.ResultBlock;

public class Execution {
    
    private final String command;
    private final ResultBlock output;
    private final WorksheetCommandExecutor executor;
    
    public Execution(Command command, WorksheetCommandExecutor executor) {
        this.command = command.commandText();
        output = command.block();
        this.executor = executor;
        
        output.becomeWaiting();
    }
    
    public ResultBlock start() {
        output.reset();
        executor.executeCommand(command);
        return output;
    }
    
}
