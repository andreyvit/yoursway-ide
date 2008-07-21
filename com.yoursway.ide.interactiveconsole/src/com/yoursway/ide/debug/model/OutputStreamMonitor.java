package com.yoursway.ide.debug.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.yoursway.ide.debug.model.ExternalDebug.OutputCompletedMonitor;

public class OutputStreamMonitor {
    
    private final Thread thread;
    
    public OutputStreamMonitor(InputStream inputStream, final IOutputListener outputter, final boolean error,
            final OutputCompletedMonitor outputCompletedMonitor) {
        //> encoding
        
        final Reader reader = new BufferedReader(new InputStreamReader(inputStream));
        
        thread = new Thread(OutputStreamMonitor.class.getSimpleName()) {
            
            @Override
            public void run() {
                try {
                    char[] cbuf = new char[256]; //! magic
                    
                    while (true) {
                        int read = reader.read(cbuf);
                        if (read == -1)
                            break;
                        
                        outputter.outputted(String.copyValueOf(cbuf, 0, read), error);
                        outputCompletedMonitor.output();
                    }
                    
                } catch (IOException e) {
                    e.printStackTrace(); //!
                }
            }
            
        };
        
    }
    
    public void start() {
        thread.start();
    }
    
}
