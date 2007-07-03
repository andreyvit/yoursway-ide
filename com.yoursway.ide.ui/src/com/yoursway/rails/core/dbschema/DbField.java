package com.yoursway.rails.core.dbschema;

import com.yoursway.rails.utils.schemaparser.FieldInfo;

public class DbField {
    
    private final FieldInfo fieldInfo;
    
    public DbField(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }
    
    public String getName() {
        return fieldInfo.name;
    }
    
    public String getType() {
        return fieldInfo.type;
    }
    
    /**
     * Should include field limits, enum values etc.
     */
    public String getFullDisplayType() {
        return getType();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fieldInfo == null) ? 0 : fieldInfo.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final DbField other = (DbField) obj;
        if (fieldInfo == null) {
            if (other.fieldInfo != null)
                return false;
        } else if (!fieldInfo.equals(other.fieldInfo))
            return false;
        return true;
    }
    
}
