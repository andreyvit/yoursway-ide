package com.yoursway.ide.interactiveconsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class OutputStreamMonitor {
    
    public OutputStreamMonitor(InputStream inputStream, final IOutputter outputter) {
        //> encoding
        
        final Reader reader = new BufferedReader(new InputStreamReader(inputStream));
        
        Thread thread = new Thread() {
            
            @Override
            public void run() {
                try {
                    char[] cbuf = new char[256]; //! magic
                    
                    while (true) {
                        int read = reader.read(cbuf);
                        if (read == -1)
                            break;
                        
                        outputter.output(String.copyValueOf(cbuf, 0, read));
                    }
                    
                    outputter.output("TERMINATED"); //>
                    
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        };
        
        thread.start();
    }
    
}
