package com.yoursway.ide.rhtml.internal.contenttype;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.eclipse.wst.sse.core.internal.encoding.CodedIO;

/**
 * This is an "adapter" class, simply to get in input stream to act like a
 * reader. We could not use InputStreamReader directly because its internal
 * buffers are not controllable, and it sometimes pulls too much out of input
 * stream (even when it wasn't needed for our purposes).
 * 
 * The use of this class is highly specialized and by not means meant to be
 * general purpose. Its use is restricted to those cases where the input stream
 * can be regarded as ascii just long enough to determine what the real encoding
 * should be.
 */

public class ByteReader extends Reader {
    
    public static final int DEFAULT_BUFFER_SIZE = CodedIO.MAX_BUF_SIZE;
    
    protected byte[] fBuffer;
    
    protected InputStream fInputStream;
    
    protected ByteReader() {
        super();
    }
    
    public ByteReader(InputStream inputStream) {
        this(inputStream, DEFAULT_BUFFER_SIZE);
        if (!inputStream.markSupported()) {
            throw new IllegalArgumentException("ByteReader is required to have a resettable stream"); //$NON-NLS-1$
        }
    }
    
    public ByteReader(InputStream inputStream, int size) {
        this.fInputStream = inputStream;
        if (!inputStream.markSupported()) {
            throw new IllegalArgumentException("ByteReader is required to have a resettable stream"); //$NON-NLS-1$
        }
        this.fBuffer = new byte[size];
        
    }
    
    @Override
    public void close() throws IOException {
        this.fInputStream.close();
    }
    
    @Override
    public void mark(int readAheadLimit) {
        this.fInputStream.mark(readAheadLimit);
    }
    
    @Override
    public boolean markSupported() {
        return true;
    }
    
    @Override
    public int read() throws IOException {
        int b0 = this.fInputStream.read();
        return (b0 & 0x00FF);
    }
    
    @Override
    public int read(char ch[], int offset, int length) throws IOException {
        if (length > this.fBuffer.length) {
            length = this.fBuffer.length;
        }
        
        int count = this.fInputStream.read(this.fBuffer, 0, length);
        
        for (int i = 0; i < count; i++) {
            int b0 = this.fBuffer[i];
            // the 0x00FF is to "lose" the negative bits filled in the byte to
            // int conversion
            // (and which would be there if cast directly from byte to char).
            char c0 = (char) (b0 & 0x00FF);
            ch[offset + i] = c0;
        }
        return count;
    }
    
    @Override
    public boolean ready() throws IOException {
        return this.fInputStream.available() > 0;
    }
    
    @Override
    public void reset() throws IOException {
        this.fInputStream.reset();
    }
    
    @Override
    public long skip(long n) throws IOException {
        return this.fInputStream.skip(n);
    }
    
}
