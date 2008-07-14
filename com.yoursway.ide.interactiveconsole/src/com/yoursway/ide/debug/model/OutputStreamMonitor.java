package com.yoursway.ide.debug.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public class OutputStreamMonitor {
    
    private final Thread thread;
    
    public OutputStreamMonitor(InputStream inputStream, final IOutputListener outputter, final boolean error) {
        //> encoding
        
        final Reader reader = new BufferedReader(new InputStreamReader(inputStream));
        
        thread = new Thread() {
            
            @Override
            public void run() {
                try {
                    char[] cbuf = new char[256]; //! magic
                    
                    while (true) {
                        int read = reader.read(cbuf);
                        if (read == -1)
                            break;
                        
                        outputter.outputted(String.copyValueOf(cbuf, 0, read), error);
                    }
                    
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        };
        
    }
    
    public void start() {
        thread.start();
    }
    
}
