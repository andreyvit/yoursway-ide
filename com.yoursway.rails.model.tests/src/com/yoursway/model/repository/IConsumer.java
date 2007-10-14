package com.yoursway.model.repository;

public interface IConsumer {
    
    /**
     * <ol>
     * <li>The given resolver will report consistent results throughout the
     * execution of this method.</li>
     * <li>This method will not be re-entered, i.e. will never be called while
     * another thread is executing it.</li>
     * <li></li>
     * <li></li>
     * <li></li>
     * </ol>
     * 
     * @param resolver
     */
    void consume(IResolver resolver);
    
}
