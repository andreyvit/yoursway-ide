package com.yoursway.ide.interactiveconsole;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;

public class ExternalDebug extends DebugWithHistoryCompletion {
    
    private Process process;
    private final BufferedWriter writer;
    
    public ExternalDebug(String executable, CommandHistory history) {
        super(history);
        
        try {
            process = Runtime.getRuntime().exec(executable);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        OutputStream outputStream = process.getOutputStream();
        writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        
        InputStream inputStream = process.getInputStream();
        final Reader reader = new BufferedReader(new InputStreamReader(inputStream));
        
        //> error stream
        
        Thread thread = new Thread() {
            
            @Override
            public void run() {
                try {
                    char[] cbuf = new char[256]; //! magic
                    
                    while (true) {
                        int read = reader.read(cbuf);
                        if (read == -1)
                            break;
                        
                        outputString(String.copyValueOf(cbuf, 0, read));
                    }
                    
                    outputString("TERMINATED"); //>
                    
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        };
        
        thread.start();
        
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
