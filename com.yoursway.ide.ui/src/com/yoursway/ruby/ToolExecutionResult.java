package com.yoursway.ruby;

import org.eclipse.core.runtime.Assert;

public class ToolExecutionResult {
    
    private final int exitCode;
    private final String outputData;
    private final String errorData;
    
    public ToolExecutionResult(int exitCode, String outputData, String errorData) {
        Assert.isNotNull(outputData);
        Assert.isNotNull(errorData);
        this.exitCode = exitCode;
        this.outputData = outputData;
        this.errorData = errorData;
    }
    
    public int getExitCode() {
        return exitCode;
    }
    
    /**
     * stdout of the process. Can't be null.
     * 
     * @return
     */
    public String getOutputData() {
        return outputData;
    }
    
    /**
     * stderr of the process. Can't be null.
     * 
     * @return
     */
    public String getErrorData() {
        return errorData;
    }
    
}
