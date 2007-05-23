package com.yoursway.rails.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class ComparingUpdater<K, V> {
    
    public interface IVisitor<V> {
        
        void visitAdded(V value);
        
        void visitRemoved(V value);
        
    }
    
    private final Map<K, V> oldItems;
    
    private final Map<K, V> newItems = new HashMap<K, V>();
    
    public ComparingUpdater(Map<K, V> oldItems) {
        this.oldItems = oldItems;
    }
    
    public void accept(K key) {
        V value = oldItems.get(key);
        if (value == null)
            value = create(key);
        newItems.put(key, value);
    }
    
    protected abstract V create(K key);
    
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
