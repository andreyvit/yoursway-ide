package com.yoursway.ruby.wala.indentedwriter;

import java.io.PrintWriter;
import java.io.Writer;

public class IndentedPrintWriter extends PrintWriter {
    
    protected final boolean autoFlush;

    public IndentedPrintWriter(Writer out, boolean autoFlush) {
        super(new IndentedWriter(out), autoFlush);
        this.autoFlush = autoFlush;
    }

    public IndentedPrintWriter(Writer out) {
        this(out, false);
    }
    
    IndentedPrintWriter(IndentedPrintWriter impl, boolean autoFlush) {
        super(impl, autoFlush);
        this.autoFlush = autoFlush;
    }
    
    public IndentedPrintWriter indent() {
        return new IndentedPrintWriter(((IndentedWriter) out).indent(), autoFlush);
    }
    
}
