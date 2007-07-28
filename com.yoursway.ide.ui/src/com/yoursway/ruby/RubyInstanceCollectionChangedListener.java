package com.yoursway.ruby;

/**
 * Emits the events about the changing Ruby instances list.
 */
public interface RubyInstanceCollectionChangedListener {
    
    /**
     * Fires if Ruby instance has been added to the list.
     * 
     * @param rubyInstance
     */
    void rubyInstanceAdded(RubyInstance ruby);
    
    /**
     * Fires if Ruby instance has been removed from the list.
     * 
     * @param rubyInstance
     */
    void rubyInstanceRemoved(RubyInstance ruby);
}
