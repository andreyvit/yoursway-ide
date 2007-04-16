package com.yoursway.rails.model.internal;

import com.yoursway.rails.model.IRailsField;
import com.yoursway.rails.model.IRailsFieldsCollection;
import com.yoursway.rails.utils.schemaparser.FieldInfo;

public class RailsField extends RailsElement implements IRailsField {
    
    private final IRailsFieldsCollection parent;
    private final String name;
    private String type;
    
    public RailsField(IRailsFieldsCollection parent, String name) {
        this.parent = parent;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    public void refresh(FieldInfo fieldInfo) {
        type = fieldInfo.type;
    }
    
}
