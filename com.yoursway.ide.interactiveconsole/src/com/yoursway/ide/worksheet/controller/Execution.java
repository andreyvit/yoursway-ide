package com.yoursway.ide.worksheet.controller;

import com.yoursway.ide.debug.model.IDebug;
import com.yoursway.ide.worksheet.view.ResultInsertion;

public class Execution {
    
    private final String command;
    private final ResultInsertion output;
    private final IDebug debug;
    
    public Execution(String command, ResultInsertion output, IDebug debug) {
        this.command = command;
        this.output = output;
        this.debug = debug;
        
        output.becomeWaiting();
    }
    
    public ResultInsertion start() {
        output.reset();
        debug.executeCommand(command);
        return output;
    }
    
}
