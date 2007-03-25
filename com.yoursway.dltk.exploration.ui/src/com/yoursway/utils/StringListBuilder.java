package com.yoursway.utils;

public class StringListBuilder {
    
    public static final String SPACE = " ";
    
    public static final String COMMA = ",";
    
    public static final String COMMA_SPACE = ", ";
    
    private StringBuilder builder = new StringBuilder();
    private final String delimiter;
    
    public StringListBuilder(String delimiter) {
        this.delimiter = delimiter;
    }
    
    public StringListBuilder append(char[] str, int offset, int len) {
        autoAppendDelimiter();
        builder.append(str, offset, len);
        return this;
    }
    
    public StringListBuilder append(char[] str) {
        autoAppendDelimiter();
        builder.append(str);
        return this;
    }
    
    public StringListBuilder append(CharSequence s, int start, int end) {
        autoAppendDelimiter();
        builder.append(s, start, end);
        return this;
    }
    
    public StringListBuilder append(CharSequence s) {
        autoAppendDelimiter();
        builder.append(s);
        return this;
    }
    
    public StringListBuilder append(Object obj) {
        autoAppendDelimiter();
        builder.append(obj);
        return this;
    }
    
    public StringListBuilder append(String str) {
        autoAppendDelimiter();
        builder.append(str);
        return this;
    }
    
    private void autoAppendDelimiter() {
        if (builder.length() > 0)
            builder.append(delimiter);
    }
    
    public StringBuilder getBuilder() {
        return builder;
    }

    public String toString() {
        return builder.toString();
    }
    
}
