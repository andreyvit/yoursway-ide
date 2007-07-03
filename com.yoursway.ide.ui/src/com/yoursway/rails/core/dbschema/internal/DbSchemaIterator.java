package com.yoursway.rails.core.dbschema.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;

import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.rails.utils.schemaparser.RubySchemaParser;
import com.yoursway.rails.utils.schemaparser.SchemaInfo;
import com.yoursway.rails.utils.schemaparser.TableInfo;
import com.yoursway.ruby.model.RubyFactory;
import com.yoursway.ruby.model.RubyFile;
import com.yoursway.utils.RailsNamingConventions;

public class DbSchemaIterator {
    
    private final IDbTablesRequestor requestor;
    //    private final RailsProject railsProject;
    private final IFile schemaFile;
    private int schemaVersion;
    
    public DbSchemaIterator(RailsProject railsProject, IDbTablesRequestor requestor) {
        Assert.isNotNull(railsProject);
        Assert.isNotNull(requestor);
        
        //        this.railsProject = railsProject;
        this.requestor = requestor;
        schemaFile = railsProject.getProject().getFile(RailsNamingConventions.DB_SCHEMA_RB_PATH);
    }
    
    public void build() {
        // TODO: this is a source of race conditions, rewrite with proper CoreException handling
        if (!schemaFile.exists())
            return;
        RubyFile schemaRubyFile = RubyFactory.instance().createFile(schemaFile);
        ModuleDeclaration ast = schemaRubyFile.getAST();
        RubySchemaParser schemaParser = new RubySchemaParser();
        schemaParser.traverse(ast);
        SchemaInfo schema = schemaParser.getResultingSchema();
        schemaVersion = schema.getSchemaVersion();
        
        for (TableInfo tableInfo : schema.tables)
            requestor.accept(tableInfo);
    }
    
    public int getSchemaVersion() {
        return schemaVersion;
    }
    
}
