package com.yoursway.rails.utils.schemaparser;

import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.ruby.ast.HashExpression;

import com.yoursway.utils.HumaneASTVisitor;
import com.yoursway.utils.RubyASTUtils;

public class RubySchemaParser extends HumaneASTVisitor {
    
    private TableInfo currentTable;
    
    private int tableDefinitionLevel = -1;
    
    private final SchemaInfo schema = new SchemaInfo();
    
    public SchemaInfo getResultingSchema() {
        return schema;
    }
    
    @Override
    protected boolean enterCall(CallExpression node) {
        final String methodName = node.getName();
        if ("define".equals(methodName)) {
            Statement arg = RubyASTUtils.getArgumentValue(node, 0);
            if (arg instanceof HashExpression) {
                Statement versionValue = RubyASTUtils.findHashItemValue((HashExpression) arg, "version");
                Long version = RubyASTUtils.resolveConstantFixnumValue(versionValue);
                if (version != null)
                    schema.schemaVersion = version;
            }
        } else if (currentTable == null && "create_table".equals(methodName)) {
            String name = RubyASTUtils.resolveConstantStringArgument(node, 0);
            if (name != null) {
                currentTable = new TableInfo(name);
                tableDefinitionLevel = getNestingLevel();
                schema.tables.add(currentTable);
            }
        } else if (currentTable != null && "column".equals(methodName)) {
            String name = RubyASTUtils.resolveConstantStringArgument(node, 0);
            String type = RubyASTUtils.resolveConstantStringArgument(node, 1);
            if (name != null) {
                FieldInfo field = new FieldInfo();
                field.name = name;
                field.type = type;
                currentTable.fields.add(field);
            }
        }
        return true;
    }
    
    @Override
    protected void nestedLevelDropped(int newLevel) {
        if (newLevel < tableDefinitionLevel) {
            tableDefinitionLevel = -1;
            currentTable = null;
        }
    }
}
