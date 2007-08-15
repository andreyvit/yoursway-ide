package com.yoursway.ruby.wala.indentedwriter;

import java.io.IOException;
import java.io.Writer;

import org.eclipse.core.runtime.Assert;

public class IndentedWriter extends Writer {

    protected static class Master {
        
        private final Writer out;
        private boolean startOfLine = true;
        private Object lastKey;
        
        private final static String EOL = System.getProperty("line.separator");

        Master(Writer out) {
            this.out = out;
        }
        
        public void flush() throws IOException {
            out.flush();
        }

        public void write(Object key, int indent, char[] cbuf, final int off, final int len) throws IOException {
            endLineIfKeysDiffer(key);
            final int end = off + len;
            int breakPos = indexOfNewLine(cbuf, off, len);
            int sliceStart = off;
            while (breakPos >= 0) {
                int sliceLen = skipNewLine(cbuf, breakPos, end) - sliceStart;
                startLine(indent);
                out.write(cbuf, sliceStart, sliceLen);
                startOfLine = true;
                sliceStart += sliceLen;
                breakPos = indexOfNewLine(cbuf, sliceStart, end);
            }
            int sliceLen = end - sliceStart;
            if (sliceLen > 0) {
                startLine(indent);
                out.write(cbuf, sliceStart, sliceLen);
                startOfLine = false;
            }
        }

        private void endLineIfKeysDiffer(Object key) throws IOException {
            if (lastKey != null && key != lastKey)
                endLine();
            lastKey = key;
        }

        private int skipNewLine(char[] cbuf, int pos, int end) {
            char cur = cbuf[pos];
            Assert.isTrue(isNewLineChar(cur));
            if (pos >= end)
                return pos + 1;
            char next = cbuf[pos + 1];
            if (!isNewLineChar(next))
                return pos + 1;
            if (next == cur)
                return pos + 1;
            return pos + 2;
        }

        private void endLine() throws IOException {
            if (!startOfLine) {
                out.write(EOL);
                startOfLine = true;
            }
        }
        
        private void startLine(int indent) throws IOException {
            if (startOfLine) {
                for (int i = 0; i < indent; i++)
                    out.write("  ");
                startOfLine = false;
            }
        }

        private int indexOfNewLine(char[] cbuf, int off, int end) {
            for (int i = off; i < end; i++)
                if (isNewLineChar(cbuf[i]))
                    return i;
            return -1;
        }

        private boolean isNewLineChar(char ch) {
            return ch == '\n' || ch == '\r';
        }
        
    }

    public IndentedWriter(Writer rootOutput) {
        this(new Master(rootOutput), 0);
    }
    
    private final Master master;
    private final int indent;

    IndentedWriter(Master master, int indent) {
        this.master = master;
        this.indent = indent;
    }
    
    public IndentedWriter indent() {
        return new IndentedWriter(master, indent + 1);
    }
    
    @Override
    public void close() throws IOException {
    }

    @Override
    public void flush() throws IOException {
        master.flush();
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        master.write(this, indent, cbuf, off, len);
    }
    
}
