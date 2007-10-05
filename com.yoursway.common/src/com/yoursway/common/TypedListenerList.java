package com.yoursway.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class is a thread safe list that is designed for storing lists of
 * listeners. The implementation is optimized for minimal memory footprint,
 * frequent reads and infrequent writes. Modification of the list is
 * synchronized and relatively expensive, while accessing the listeners is very
 * fast. Readers are given access to the underlying array data structure for
 * reading, with the trust that they will not modify the underlying array.
 * <p>
 * <b>dottedmag</b>: underlying implementation changed to ArrayList, so this
 * might be a bit slower now.
 * <p>
 * <a name="same">A listener list handles the <i>same</i> listener being added
 * multiple times, and tolerates removal of listeners that are the same as other
 * listeners in the list. For this purpose, listeners can be compared with each
 * other using either equality or identity, as specified in the list
 * constructor.
 * </p>
 * <p>
 * This class is typed and modified version of
 * org.eclipse.core.runtime.ListenerList
 * </p>
 * <p>
 * The recommended code sequence for notifying all registered listeners of say,
 * <code>FooListener.eventHappened</code>, is:
 * 
 * <pre>
 * for (FooListener listener : myListenerList)
 *     listener.eventHappened(event);
 * </pre>
 * 
 * </p>
 */
public final class TypedListenerList<T> implements Iterable<T> {
    
    /**
     * Mode constant (value 0) indicating that listeners should be considered
     * the <a href="#same">same</a> if they are equal.
     */
    public static final int EQUALITY = 0;
    
    /**
     * Mode constant (value 1) indicating that listeners should be considered
     * the <a href="#same">same</a> if they are identical.
     */
    public static final int IDENTITY = 1;
    
    /**
     * Indicates the comparison mode used to determine if two listeners are
     * equivalent
     */
    private final boolean identity;
    
    /**
     * The list of listeners. Initially empty but initialized to an array of
     * size capacity the first time a listener is added. Maintains invariant:
     * listeners != null
     */
    private volatile List<T> listeners = Collections.emptyList();
    
    /**
     * Creates a listener list in which listeners are compared using equality.
     */
    public TypedListenerList() {
        this(EQUALITY);
    }
    
    /**
     * Creates a listener list using the provided comparison mode.
     * 
     * @param mode
     *            The mode used to determine if listeners are the <a
     *            href="#same">same</a>.
     */
    public TypedListenerList(int mode) {
        if (mode != EQUALITY && mode != IDENTITY)
            throw new IllegalArgumentException();
        this.identity = mode == IDENTITY;
    }
    
    /**
     * Adds a listener to this list. This method has no effect if the <a
     * href="#same">same</a> listener is already registered.
     * 
     * @param listener
     *            the non-<code>null</code> listener to add
     */
    public synchronized <L extends T> void add(L listener) {
        // This method is synchronized to protect against multiple threads adding 
        // or removing listeners concurrently. This does not block concurrent readers.
        if (listener == null)
            throw new IllegalArgumentException();
        // check for duplicates 
        for (T existingListener : listeners) {
            if (identity ? listener == existingListener : listener.equals(existingListener))
                return;
        }
        ArrayList<T> newListeners = new ArrayList<T>(listeners.size() + 1);
        newListeners.addAll(listeners);
        newListeners.add(listener);
        
        //atomic assignment
        listeners = newListeners;
    }
    
    /**
     * Returns an array containing all the registered listeners. The resulting
     * array is unaffected by subsequent adds or removes. If there are no
     * listeners registered, the result is an empty array. Use this method when
     * notifying listeners, so that any modifications to the listener list
     * during the notification will have no effect on the notification itself.
     * <p>
     * Note: Callers of this method <b>must not</b> modify the returned array.
     * 
     * @return the list of registered listeners
     */
    public Iterator<T> iterator() {
        return listeners.iterator();
    }
    
    /**
     * Returns whether this listener list is empty.
     * 
     * @return <code>true</code> if there are no registered listeners, and
     *         <code>false</code> otherwise
     */
    public boolean isEmpty() {
        return listeners.isEmpty();
    }
    
    /**
     * Removes a listener from this list. Has no effect if the <a
     * href="#same">same</a> listener was not already registered.
     * 
     * @param listener
     *            the non-<code>null</code> listener to remove
     */
    public synchronized <E extends T> void remove(E listener) {
        // This method is synchronized to protect against multiple threads adding 
        // or removing listeners concurrently. This does not block concurrent readers.
        if (listener == null)
            throw new IllegalArgumentException();
        
        for (T existingListener : listeners) {
            if (identity ? listener == existingListener : listener.equals(existingListener)) {
                // Optimization.
                if (listeners.size() == 1) {
                    listeners = Collections.emptyList();
                } else {
                    ArrayList<T> newListeners = new ArrayList<T>(listeners.size());
                    newListeners.addAll(listeners);
                    // We can remove existingListener even if we compare objects'
                    // identity because we just got it from the collection
                    newListeners.remove(existingListener);
                    // atomic assignment
                    listeners = newListeners;
                }
                return;
            }
        }
    }
    
    /**
     * Returns the number of registered listeners.
     * 
     * @return the number of registered listeners
     */
    public int size() {
        return listeners.size();
    }
    
    /**
     * Removes all listeners from this list.
     */
    public synchronized void clear() {
        listeners = Collections.emptyList();
    }
}
