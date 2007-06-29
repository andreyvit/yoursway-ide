package com.yoursway.ruby;

public class ToolExecutionResult {
    
    private final int exitCode;
    private final String outputData;
    private final String errorData;
    
    public ToolExecutionResult(int exitCode, String outputData, String errorData) {
        this.exitCode = exitCode;
        this.outputData = outputData;
        this.errorData = errorData;
    }
    
    public int getExitCode() {
        return exitCode;
    }
    
    public String getOutputData() {
        return outputData;
    }
    
    public String getErrorData() {
        return errorData;
    }
    
}
