package com.yoursway.rails.utils.schemaparser;

import java.util.ArrayList;
import java.util.Collection;

public class SchemaInfo {
    
    public Long schemaVersion;
    
    public final Collection<TableInfo> tables = new ArrayList<TableInfo>();
    
    public int getSchemaVersion() {
        return (schemaVersion == null ? -1 : schemaVersion.intValue());
    }
    
}
