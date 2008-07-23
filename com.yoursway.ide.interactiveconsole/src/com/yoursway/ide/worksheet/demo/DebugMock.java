package com.yoursway.ide.worksheet.demo;

import com.yoursway.ide.oldconsole.CommandHistory;
import com.yoursway.ide.worksheet.executors.standard.CommandExecutorWithHistoryCompletion;

public class DebugMock extends CommandExecutorWithHistoryCompletion {
    
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
