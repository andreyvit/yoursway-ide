package com.yoursway.rails.model.internal.infos.handles;

public abstract class RailsModelException extends Exception {
    
    public RailsModelException() {
        super();
    }
    
    public RailsModelException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public RailsModelException(String message) {
        super(message);
    }
    
    public RailsModelException(Throwable cause) {
        super(cause);
    }
    
}
