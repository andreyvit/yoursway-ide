package com.yoursway.rails.model.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.yoursway.rails.model.IRailsField;
import com.yoursway.rails.model.IRailsFieldsCollection;
import com.yoursway.rails.model.IRailsTable;
import com.yoursway.rails.utils.schemaparser.FieldInfo;
import com.yoursway.rails.utils.schemaparser.TableInfo;

public class RailsTable extends RailsElement implements IRailsTable, IRailsFieldsCollection {
    
    private final RailsSchema railsSchema;
    private final String name;
    private final Map<String, RailsField> fields = new HashMap<String, RailsField>();
    
    public RailsTable(RailsSchema railsSchema, String name) {
        this.railsSchema = railsSchema;
        this.name = name;
    }
    
    public IRailsFieldsCollection getFields() {
        return this;
    }
    
    public String getName() {
        return name;
    }
    
    public void refresh(TableInfo tableInfo) {
        Set<RailsField> zombies = new HashSet<RailsField>(fields.values());
        for (FieldInfo fieldInfo : tableInfo.fields) {
            RailsField railsField = fields.get(fieldInfo.name);
            if (railsField != null)
                zombies.remove(railsField);
            else {
                railsField = new RailsField(this, fieldInfo.name);
                fields.put(railsField.getName(), railsField);
            }
            railsField.refresh(fieldInfo);
        }
        for (RailsField railsField : zombies) {
            fields.remove(railsField.getName());
        }
    }
    
    public boolean areItemsKnown() {
        return true;
    }
    
    public Collection<? extends IRailsField> getItems() {
        return fields.values();
    }
    
    public boolean hasItems() {
        return fields.size() > 0;
    }
    
}
