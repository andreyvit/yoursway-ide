package com.yoursway.ide.interactiveconsole;

public class DebugMock extends DebugWithHistoryCompletion {
    
    public DebugMock(CommandHistory history) {
        super(history);
        
        Thread outputter = new Thread() {
            
            @Override
            public void run() {
                try {
                    while (true) {
                        sleep(10000);
                        output("hahaha\n", true);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        };
        
        outputter.start();
    }
    
    public void executeCommand(String command) {
        output("ok: " + command + "\n");
    }
    
}
