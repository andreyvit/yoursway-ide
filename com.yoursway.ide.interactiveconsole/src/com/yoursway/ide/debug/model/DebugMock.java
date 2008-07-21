package com.yoursway.ide.debug.model;

import com.yoursway.ide.interactiveconsole.CommandHistory;

public class DebugMock extends DebugWithHistoryCompletion {
    
    public DebugMock(CommandHistory history) {
        super(history);
        
        Thread outputter = new Thread(DebugMock.class.getSimpleName() + " outputter") {
            
            @Override
            public void run() {
                try {
                    while (true) {
                        sleep(10000);
                        output("hahaha\n", true);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace(); //!
                }
            }
            
        };
        
        outputter.start();
    }
    
    public void executeCommand(String command) {
        output("ok: " + command + "\n");
    }
    
}
