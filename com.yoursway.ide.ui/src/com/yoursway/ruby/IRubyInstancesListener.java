package com.yoursway.ruby;

public interface IRubyInstancesListener {
    
    void rubyInstanceAdded(RubyInstance rubyInstance);
    
    void rubyInstanceRemoved(RubyInstance rubyInstance);
    
}
