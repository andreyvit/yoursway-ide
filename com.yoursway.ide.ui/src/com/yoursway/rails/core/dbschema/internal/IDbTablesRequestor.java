/**
 * 
 */
package com.yoursway.rails.core.dbschema.internal;

import com.yoursway.rails.utils.schemaparser.TableInfo;

public interface IDbTablesRequestor {
    
    void accept(TableInfo tableInfo);
    
}