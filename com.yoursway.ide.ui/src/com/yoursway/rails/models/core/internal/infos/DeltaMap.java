package com.yoursway.rails.models.core.internal.infos;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class DeltaMap<K, V> implements Map<K, V> {
    
    public interface DeltaMapListener<K> {
        
        void visitAdded(K key);
        
        void visitRemoved(K key);
        
        void visitChanged(K key);
        
    }
    
    private final Map<K, V> map;
    
    public DeltaMap(Map<K, V> map) {
        this.map = map;
    }
    
    public void clear() {
        map.clear();
    }
    
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }
    
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }
    
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
    
    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }
    
    public V get(Object key) {
        return map.get(key);
    }
    
    @Override
    public int hashCode() {
        return map.hashCode();
    }
    
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    public Set<K> keySet() {
        return map.keySet();
    }
    
    public V put(K key, V value) {
        return map.put(key, value);
    }
    
    public void putAll(Map<? extends K, ? extends V> t) {
        map.putAll(t);
    }
    
    public V remove(Object key) {
        return map.remove(key);
    }
    
    public int size() {
        return map.size();
    }
    
    public Collection<V> values() {
        return map.values();
    }
    
}
