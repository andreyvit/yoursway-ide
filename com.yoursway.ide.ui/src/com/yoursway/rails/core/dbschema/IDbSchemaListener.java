package com.yoursway.rails.core.dbschema;

public interface IDbSchemaListener {
    
    void tableAdded(DbTable dbTable);
    
    void tableRemoved(DbTable dbTable);
    
    void tableChanged(DbTable dbTable);
    
}
