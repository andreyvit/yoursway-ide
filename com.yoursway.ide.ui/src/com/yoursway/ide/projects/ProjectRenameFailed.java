package com.yoursway.ide.projects;

public class ProjectRenameFailed extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public ProjectRenameFailed() {
        super();
    }
    
    public ProjectRenameFailed(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ProjectRenameFailed(String message) {
        super(message);
    }
    
    public ProjectRenameFailed(Throwable cause) {
        super(cause);
    }
    
}
