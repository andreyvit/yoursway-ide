package com.yoursway.rails.utils.schemaparser;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ruby.ast.RubyHashExpression;

import com.yoursway.rails.commons.RubyAstTraverser;
import com.yoursway.rails.commons.RubyAstVisitor;
import com.yoursway.rails.commons.RubyRootAstVisitor;
import com.yoursway.utils.RubyASTUtils;

public class RubySchemaParser {
    
    private final SchemaInfo schema = new SchemaInfo();
    
    public SchemaInfo getResultingSchema() {
        return schema;
    }
    
    class RootContext extends RubyRootAstVisitor {
        
        @Override
        protected RubyAstVisitor<?> enterCall(CallExpression node) {
            String methodName = node.getName();
            if ("define".equals(methodName)) {
                ASTNode arg = RubyASTUtils.getArgumentValue(node, 0);
                if (arg instanceof RubyHashExpression) {
                    ASTNode versionValue = RubyASTUtils
                            .findHashItemValue((RubyHashExpression) arg, "version");
                    Long version = RubyASTUtils.resolveConstantFixnumValue(versionValue);
                    if (version != null)
                        schema.schemaVersion = version;
                }
            } else if ("create_table".equals(methodName)) {
                String name = RubyASTUtils.resolveConstantStringArgument(node, 0);
                if (name != null) {
                    TableInfo info = new TableInfo(name);
                    schema.tables.add(info);
                    return new TableContext(this, info);
                }
            }
            return this;
        }
        
    }
    
    class TableContext extends RubyAstVisitor<ASTNode> {
        
        private final TableInfo currentTable;
        
        public TableContext(RubyAstVisitor<?> parentVisitor, TableInfo currentTable) {
            super(parentVisitor);
            this.currentTable = currentTable;
        }
        
        @Override
        protected RubyAstVisitor<?> enterCall(CallExpression node) {
            final String methodName = node.getName();
            if ("column".equals(methodName)) {
                String name = RubyASTUtils.resolveConstantStringArgument(node, 0);
                String type = RubyASTUtils.resolveConstantStringArgument(node, 1);
                if (name != null) {
                    FieldInfo field = new FieldInfo();
                    field.name = name;
                    field.type = type;
                    currentTable.fields.add(field);
                }
            }
            return null;
        }
        
    }
    
    public void traverse(ModuleDeclaration module) {
        RubyAstTraverser traverser = new RubyAstTraverser();
        traverser.traverse(module, new RootContext());
    }
    
}
