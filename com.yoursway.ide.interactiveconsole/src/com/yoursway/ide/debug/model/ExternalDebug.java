package com.yoursway.ide.debug.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.yoursway.ide.interactiveconsole.CommandHistory;

public class ExternalDebug extends DebugWithHistoryCompletion {
    
    private Process process;
    private BufferedWriter writer;
    private OutputCompletedMonitor outputCompletedMonitor;
    
    public ExternalDebug(String executable, CommandHistory history) {
        super(history);
        
        try {
            process = Runtime.getRuntime().exec(executable);
            
            OutputStream outputStream = process.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            
            outputCompletedMonitor = new OutputCompletedMonitor();
            
            new OutputStreamMonitor(process.getInputStream(), outputter(), false, outputCompletedMonitor)
                    .start();
            new OutputStreamMonitor(process.getErrorStream(), outputter(), true, outputCompletedMonitor)
                    .start();
            
            Thread processMonitor = new Thread() {
                @Override
                public void run() {
                    try {
                        process.waitFor();
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    } finally {
                        terminated();
                    }
                }
            };
            
            outputCompletedMonitor.start();
            processMonitor.start();
            
        } catch (IOException e) {
            //!
            e.printStackTrace();
        }
    }
    
    public void executeCommand(String command) {
        try {
            outputCompletedMonitor.reset();
            
            writer.write(command + '\n');
            writer.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    protected void terminated() {
        output("TERMINATED", true);
        super.terminated();
    }
    
    public final class OutputCompletedMonitor extends Thread {
        private boolean reset = false;
        private boolean outputted = true;
        
        @Override
        public synchronized void run() {
            try {
                while (true) {
                    while (!reset)
                        wait();
                    
                    outputted = true;
                    while (outputted) {
                        outputted = false;
                        wait(1000);
                    }
                    
                    reset = false;
                    completed();
                }
            } catch (InterruptedException e) {
                interrupted();
            }
            
        }
        
        public synchronized void output() {
            outputted = true;
            notify();
        }
        
        public synchronized void reset() {
            reset = true;
            notify();
        }
        
    }
    
}
