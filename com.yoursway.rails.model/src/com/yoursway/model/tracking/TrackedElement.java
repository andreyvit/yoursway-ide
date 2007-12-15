package com.yoursway.model.tracking;

import com.yoursway.model.repository.IModelElement;

public class TrackedElement<T extends Enum<T>> implements IModelElement {
    
    private final Object[] data;
    
    public TrackedElement(Class<T> propertiesKlass) {
        data = new Object[propertiesKlass.getFields().length];
    }
    
    public Object get(T property) {
        return data[property.ordinal()];
    }
    
    public void put(T property, Object value) {
        data[property.ordinal()] = value;
    }
    
}
