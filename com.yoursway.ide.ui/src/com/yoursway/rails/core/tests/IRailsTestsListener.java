package com.yoursway.rails.core.tests;

public interface IRailsTestsListener {
    
    void testAdded(RailsTest railsTest);
    
    void testRemoved(RailsTest railsTest);
    
    void reconcile(RailsTest railsTest);
    
}
