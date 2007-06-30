package com.yoursway.rails.old_code_repository;

import com.yoursway.rails.utils.schemaparser.TableInfo;

@Deprecated
public class RailsTable {
    
    private final RailsSchema railsSchema;
    private final String name;
    
    public RailsTable(RailsSchema railsSchema, String name) {
        this.railsSchema = railsSchema;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void refresh(TableInfo tableInfo) {
        //        Set<RailsField> zombies = new HashSet<RailsField>(fields.values());
        //        for (FieldInfo fieldInfo : tableInfo.fields) {
        //            RailsField railsField = fields.get(fieldInfo.name);
        //            if (railsField != null)
        //                zombies.remove(railsField);
        //            else {
        //                railsField = new RailsField(this, fieldInfo.name);
        //                fields.put(railsField.getName(), railsField);
        //            }
        //            railsField.refresh(fieldInfo);
        //        }
        //        for (RailsField railsField : zombies) {
        //            fields.remove(railsField.getName());
        //        }
    }
    
    public boolean areItemsKnown() {
        return true;
    }
    
}
