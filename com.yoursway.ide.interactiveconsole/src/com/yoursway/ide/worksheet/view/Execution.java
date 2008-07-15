package com.yoursway.ide.worksheet.view;

import com.yoursway.ide.debug.model.IDebug;

public class Execution {
    
    private final String command;
    private final Insertion output;
    private final IDebug debug;
    
    public Execution(String command, Insertion output, IDebug debug) {
        this.command = command;
        this.output = output;
        this.debug = debug;
        
        output.setText("...");
    }
    
    public Insertion start() {
        output.setText("");
        debug.executeCommand(command);
        return output;
    }
    
}
