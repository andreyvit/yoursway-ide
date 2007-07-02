package com.yoursway.rails.core.internal.support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class ComparingUpdater<D, K, V> {
    
    public interface IVisitor<V> {
        
        void visitAdded(V value);
        
        void visitRemoved(V value);
        
    }
    
    private final Map<K, V> oldItems;
    
    private final Map<K, V> newItems = new HashMap<K, V>();
    
    public ComparingUpdater(Map<K, V> oldItems) {
        this.oldItems = oldItems;
    }
    
    public void accept(D data) {
        K key = getKey(data);
        V value = oldItems.get(key);
        if (value == null)
            value = create(data);
        else
            update(value, data);
        newItems.put(key, value);
    }
    
    protected abstract V create(D data);
    
    protected abstract void update(V value, D data);
    
    protected abstract K getKey(D data);
    
    public Map<K, V> getNewItems() {
        return newItems;
    }
    
    public void visitChanges(IVisitor<V> visitor) {
        HashSet<V> oldItemsSet = new HashSet<V>(oldItems.values());
        HashSet<V> newItemsSet = new HashSet<V>(newItems.values());
        @SuppressWarnings("unchecked")
        HashSet<V> deletedItemsSet = (HashSet<V>) oldItemsSet.clone();
        deletedItemsSet.removeAll(newItemsSet);
        newItemsSet.removeAll(oldItemsSet);
        for (V value : deletedItemsSet)
            visitor.visitRemoved(value);
        for (V value : newItemsSet)
            visitor.visitAdded(value);
    }
    
}
