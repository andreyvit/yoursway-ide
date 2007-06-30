package com.yoursway.rails.old_code_repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;

import com.yoursway.rails.models.project.RailsProject;
import com.yoursway.rails.utils.schemaparser.RubySchemaParser;
import com.yoursway.rails.utils.schemaparser.SchemaInfo;
import com.yoursway.rails.utils.schemaparser.TableInfo;
import com.yoursway.ruby.model.RubyFactory;
import com.yoursway.ruby.model.RubyFile;
import com.yoursway.utils.PathUtils;
import com.yoursway.utils.RailsNamingConventions;

@Deprecated
public class RailsSchema {
    
    private RailsProject railsProject;
    
    private boolean isOpen = false;
    
    private final Map<String, RailsTable> tables = new HashMap<String, RailsTable>();
    
    private int schemaVersion = -1;
    
    private void refresh() {
        isOpen = true;
        Set<RailsTable> zombies = new HashSet<RailsTable>(tables.values());
        
        IFile schemaFile = getRubySchemaFile();
        if (schemaFile != null && schemaFile.exists()) {
            RubyFile schemaRubyFile = RubyFactory.instance().createFile(schemaFile);
            ModuleDeclaration ast = schemaRubyFile.getAST();
            RubySchemaParser schemaParser = new RubySchemaParser();
            schemaParser.traverse(ast);
            SchemaInfo schema = schemaParser.getResultingSchema();
            schemaVersion = schema.getSchemaVersion();
            
            for (TableInfo tableInfo : schema.tables) {
                RailsTable railsTable = tables.get(tableInfo.name);
                if (railsTable == null) {
                    railsTable = new RailsTable(this, tableInfo.name);
                    tables.put(railsTable.getName(), railsTable);
                } else
                    zombies.remove(railsTable);
                railsTable.refresh(tableInfo);
            }
        }
        for (RailsTable railsTable : zombies) {
            tables.remove(railsTable.getName());
        }
    }
    
    public boolean hasItems() {
        return true;
    }
    
    public void reconcile(IResourceDelta delta) {
        IResourceDelta member = delta.findMember(new Path(RailsNamingConventions.DB_SCHEMA_RB));
        if (member != null)
            refresh();
    }
    
    private IFile getRubySchemaFile() {
        return PathUtils.getFile(railsProject.getProject(), RailsNamingConventions.DB_SCHEMA_RB);
    }
    
    public int getVersion() {
        open();
        return schemaVersion;
    }
    
    public void open() {
        if (!isOpen)
            refresh();
    }
    
}
