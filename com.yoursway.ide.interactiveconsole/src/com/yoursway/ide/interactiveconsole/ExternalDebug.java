package com.yoursway.ide.interactiveconsole;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ExternalDebug extends DebugWithHistoryCompletion {
    
    private Process process;
    private BufferedWriter writer;
    
    public ExternalDebug(String executable, CommandHistory history) {
        super(history);
        
        try {
            process = Runtime.getRuntime().exec(executable);
            
            OutputStream outputStream = process.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            
            new OutputStreamMonitor(process.getInputStream(), outputter(), false).start();
            new OutputStreamMonitor(process.getErrorStream(), outputter(), true).start();
            
        } catch (IOException e) {
            //!
            e.printStackTrace();
        }
    }
    
    public void executeCommand(String command) {
        try {
            writer.write(command + '\n');
            writer.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
