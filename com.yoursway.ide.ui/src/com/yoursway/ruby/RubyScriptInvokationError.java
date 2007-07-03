/**
 * 
 */
package com.yoursway.ruby;

public class RubyScriptInvokationError extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public RubyScriptInvokationError() {
        super();
    }
    
    public RubyScriptInvokationError(String message, Throwable cause) {
        super(message, cause);
    }
    
    public RubyScriptInvokationError(String message) {
        super(message);
    }
    
    public RubyScriptInvokationError(Throwable cause) {
        super(cause);
    }
    
}