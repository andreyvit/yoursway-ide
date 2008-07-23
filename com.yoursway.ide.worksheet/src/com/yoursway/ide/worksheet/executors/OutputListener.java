package com.yoursway.ide.worksheet.executors;

public interface OutputListener {
    
    void outputted(String text, boolean error);
    
    void completed();
    
}
