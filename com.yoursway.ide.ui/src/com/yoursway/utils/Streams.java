package com.yoursway.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class Streams {
    private static final int BUFSIZE = 4096;
    
    public static String readToString(InputStream is) throws IOException {
        Reader streamReader = new InputStreamReader(is);
        
        StringBuffer res = new StringBuffer();
        char[] buffer = new char[BUFSIZE];
        
        int read = streamReader.read(buffer);
        while (read != -1) {
            res.append(buffer, 0, read);
            read = streamReader.read(buffer);
        }
        streamReader.close();
        return res.toString();
    }
    
    public static void writeString(OutputStream os, String s) throws IOException {
        Writer streamWriter = new OutputStreamWriter(os);
        streamWriter.write(s);
        streamWriter.close();
    }
}
