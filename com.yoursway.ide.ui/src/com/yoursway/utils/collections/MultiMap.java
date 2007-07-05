package com.yoursway.utils.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class MultiMap<K, V> {
    
    private final Map<K, Collection<V>> data = new HashMap<K, Collection<V>>();
    
    public void put(K key, V value) {
        getCollection(key).add(value);
    }
    
    protected Collection<V> getCollection(K key) {
        Collection<V> coll = data.get(key);
        if (coll == null) {
            coll = createInnerCollection();
            data.put(key, coll);
        }
        return coll;
    }
    
    protected Collection<V> createInnerCollection() {
        return new ArrayList<V>();
    }
    
    public void clear() {
        data.clear();
    }
    
    public boolean containsKey(K key) {
        return data.containsKey(key);
    }
    
    public boolean containsValue(V value) {
        for (Collection<V> values : data.values())
            if (values.contains(value))
                return true;
        return false;
    }
    
    @Override
    public boolean equals(Object o) {
        return data.equals(o);
    }
    
    @Override
    public int hashCode() {
        return data.hashCode();
    }
    
    public boolean isEmpty() {
        return data.isEmpty();
    }
    
    public Set<K> keySet() {
        return data.keySet();
    }
    
    public Collection<V> remove(K key) {
        return data.remove(key);
    }
    
    public int valuesCount() {
        int size = 0;
        for (Collection<V> values : data.values())
            size += values.size();
        return size;
    }
    
    public int keysCount() {
        return data.size();
    }
    
    public Collection<Collection<V>> values() {
        return data.values();
    }
    
    public Set<Entry<K, Collection<V>>> entrySet() {
        return data.entrySet();
    }
    
}
