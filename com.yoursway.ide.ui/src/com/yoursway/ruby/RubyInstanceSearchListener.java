package com.yoursway.ruby;

/**
 * Emits the events about the Ruby search progress.
 */
public interface RubyInstanceSearchListener {
    /**
     * Fires before starting Ruby search.
     */
    void searchStarting();
    
    /**
     * Fires after finishing Ruby search.
     */
    void searchFinished();
}
