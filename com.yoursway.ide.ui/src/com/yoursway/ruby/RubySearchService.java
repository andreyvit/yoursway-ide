package com.yoursway.ruby;

/**
 * Service for searching Ruby and watching progress.
 */
public interface RubySearchService {
    void start();
    
    void addInstanceSearchListener(RubyInstanceSearchListener listener);
    
    void removeInstanceSearchListener(RubyInstanceSearchListener listener);
}
