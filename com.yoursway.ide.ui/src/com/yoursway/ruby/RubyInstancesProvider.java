package com.yoursway.ruby;

import java.util.Collection;

import com.yoursway.ruby.internal.RubyInstanceImpl;

/**
 * Service for obtaining existing Ruby instances and watching for their
 * appearance and disappearance.
 */
public interface RubyInstancesProvider {
    /**
     * Returns collection of all available Ruby instances.
     * 
     * @return Ruby instances collection.
     */
    public Collection<RubyInstanceImpl> getAll();
    
    /**
     * Subscribes listener to the event of changed list of Ruby instances.
     * 
     * @param listener
     */
    public void addRubyInstanceCollectionChangedListener(RubyInstanceCollectionChangedListener listener);
    
    /**
     * Unsubscribes listener from the event of changed list of Ruby instances.
     * 
     * @param listener
     * @throws NoSuchListenerError
     */
    public void removeRubyInstanceCollectionChangedListener(RubyInstanceCollectionChangedListener listener);
}
