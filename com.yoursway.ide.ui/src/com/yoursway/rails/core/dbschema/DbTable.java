package com.yoursway.rails.core.dbschema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.rails.utils.schemaparser.FieldInfo;
import com.yoursway.rails.utils.schemaparser.TableInfo;

public class DbTable {
    
    private final RailsProject railsProject;
    private final String name;
    private Collection<DbField> fields = new ArrayList<DbField>();
    
    public DbTable(RailsProject railsProject, TableInfo data) {
        if (railsProject == null)
            throw new IllegalArgumentException();
        if (data == null)
            throw new IllegalArgumentException();
        
        this.railsProject = railsProject;
        name = data.name;
        update(data.fields);
    }
    
    public RailsProject getRailsProject() {
        return railsProject;
    }
    
    public String getName() {
        return name;
    }
    
    public Collection<DbField> getFields() {
        return fields;
    }
    
    public boolean update(Collection<FieldInfo> fieldInfos) {
        ArrayList<DbField> newFields = new ArrayList<DbField>();
        for (FieldInfo fieldInfo : fieldInfos)
            newFields.add(new DbField(fieldInfo));
        if (fields != null) {
            HashSet<DbField> oldFieldsSet = new HashSet<DbField>(fields);
            HashSet<DbField> newFieldsSet = new HashSet<DbField>(newFields);
            if (oldFieldsSet.equals(newFieldsSet))
                return false;
        }
        fields = newFields;
        return true;
    }
    
}
