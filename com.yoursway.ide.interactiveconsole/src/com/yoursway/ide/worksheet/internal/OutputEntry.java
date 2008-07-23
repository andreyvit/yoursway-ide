package com.yoursway.ide.worksheet.internal;

public class OutputEntry {
    
    private final String text;
    private final boolean error;
    
    public OutputEntry(String text, boolean error) {
        this.text = text;
        this.error = error;
    }
    
    public String text() {
        return text;
    }
    
    public boolean error() {
        return error;
    }
    
}
