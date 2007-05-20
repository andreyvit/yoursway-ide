package com.yoursway.ruby;

public class ToolExecutionResult {
    
    private final int exitCode;
    private final String outputData;
    
    public ToolExecutionResult(int exitCode, String outputData) {
        this.exitCode = exitCode;
        this.outputData = outputData;
    }
    
    public int getExitCode() {
        return exitCode;
    }
    
    public String getOutputData() {
        return outputData;
    }
    
}
