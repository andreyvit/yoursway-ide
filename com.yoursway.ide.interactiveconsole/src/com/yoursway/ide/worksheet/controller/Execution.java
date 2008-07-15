package com.yoursway.ide.worksheet.controller;

import com.yoursway.ide.debug.model.IDebug;
import com.yoursway.ide.worksheet.view.Insertion;

public class Execution {
    
    private final String command;
    private final Insertion output;
    private final IDebug debug;
    
    public Execution(String command, Insertion output, IDebug debug) {
        this.command = command;
        this.output = output;
        this.debug = debug;
        
        output.becomeWaiting();
    }
    
    public Insertion start() {
        output.reset();
        debug.executeCommand(command);
        return output;
    }
    
}
