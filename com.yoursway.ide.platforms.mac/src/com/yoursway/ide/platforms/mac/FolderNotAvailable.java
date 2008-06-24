package com.yoursway.ide.platforms.mac;

public class FolderNotAvailable extends Exception {

    private static final long serialVersionUID = 1L;

    public FolderNotAvailable() {
        super();
    }

    public FolderNotAvailable(String message, Throwable cause) {
        super(message, cause);
    }

    public FolderNotAvailable(String message) {
        super(message);
    }

    public FolderNotAvailable(Throwable cause) {
        super(cause);
    }

}
