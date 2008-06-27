package com.yoursway.ide.application.problems;

public class Bugs {

    public static void listenerFailed(Throwable error, Object listener, String event) {
        error.printStackTrace(System.err);
    }
    
    public static void illegalCaseRecovery(Severity severity, String description) {
        System.err.println("[" + severity + "] " + description);
    }

    public static void unknownErrorRecovery(Severity severity, Throwable error) {
        System.err.println("[" + severity + "] unknown error");
        error.printStackTrace(System.err);
    }
    
}
