package com.yoursway.ide.worksheet.executors.standard;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.yoursway.ide.oldconsole.CommandHistory;
import com.yoursway.ide.worksheet.internal.OutputStreamMonitor;

public class ExternalCommandExecutor extends CommandExecutorWithHistoryCompletion {
    
    private Process process;
    private BufferedWriter writer;
    private OutputCompletedMonitor outputCompletedMonitor;
    
    public ExternalCommandExecutor(String executable, CommandHistory history) {
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
            
            //> include process name and ID (?) into the thread name
            Thread processMonitor = new Thread(ExternalCommandExecutor.class.getSimpleName()
                    + " processMonitor") {
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
            e.printStackTrace(); //!
        }
    }
    
    public void executeCommand(String command) {
        try {
            outputCompletedMonitor.reset();
            
            writer.write(command + '\n');
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace(); //!
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
        
        public OutputCompletedMonitor() {
            super(OutputCompletedMonitor.class.getSimpleName());
        }
        
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
