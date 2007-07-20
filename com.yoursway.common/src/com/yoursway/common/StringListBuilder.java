package com.yoursway.common;

/**
 * This class is an equivalent to the Perl's join($delimiter, \@args);
 */
public class StringListBuilder {
    
    public static final String SPACE = " ";
    
    public static final String COMMA = ",";
    
    public static final String COMMA_SPACE = ", ";
    
    private final StringBuilder builder = new StringBuilder();
    private final String delimiter;
    
    public StringListBuilder(String delimiter) {
        this.delimiter = delimiter;
    }
    
    public StringListBuilder append(char[] str, int offset, int len) {
        autoAppendDelimiter();
        builder.append(str, offset, len);
        return this;
    }
    
    public StringListBuilder append(CharSequence s, int start, int end) {
        autoAppendDelimiter();
        builder.append(s, start, end);
        return this;
    }
    
    public <T> StringListBuilder append(T arg) {
        autoAppendDelimiter();
        builder.append(arg);
        return this;
    }
    
    private void autoAppendDelimiter() {
        if (builder.length() > 0)
            builder.append(delimiter);
    }
    
    public StringBuilder getBuilder() {
        return builder;
    }
    
    @Override
    public String toString() {
        return builder.toString();
    }
    
}
