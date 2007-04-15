package com.yoursway.rails.utils.schemaparser;

import java.util.ArrayList;
import java.util.Collection;

public class TableInfo {
    
    public final String name;
    
    public final Collection<FieldInfo> fields = new ArrayList<FieldInfo>();
    
    public TableInfo(String name) {
        super();
        this.name = name;
    }
    
}
