package com.yoursway.ide.worksheet.internal.demo;

import java.util.Collections;
import java.util.List;

import com.yoursway.ide.worksheet.executors.WorksheetCompletionProposal;
import com.yoursway.ide.worksheet.executors.standard.AbstractWorksheetCommandExecutor;

public class ExecutorMock extends AbstractWorksheetCommandExecutor {
    
    public ExecutorMock() {
        Thread outputter = new Thread(ExecutorMock.class.getSimpleName() + " outputter") {
            
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
    
    public List<WorksheetCompletionProposal> complete(String command, int position) {
        return Collections.emptyList();
    }
    
}
