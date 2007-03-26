package com.yoursway.utils;

import java.util.List;

import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.expressions.NumericLiteral;
import org.eclipse.dltk.ast.expressions.StringLiteral;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.ruby.ast.HashExpression;
import org.eclipse.dltk.ruby.ast.HashPairExpression;
import org.eclipse.dltk.ruby.ast.SymbolReference;

public class RubyASTUtils {
    
    /**
     * @param value
     *            AST node whose value is to be resolved, can be
     *            <code>null</code>.
     * @return The resolved value, or <code>null</code> if a constant string
     *         value could not be resolved or the received argument is
     *         <code>null</code>.
     */
    public static String resolveConstantStringValue(Statement value) {
        if (value instanceof SymbolReference)
            return ((SymbolReference) value).getName();
        else if (value instanceof StringLiteral)
            return ((StringLiteral) value).getValue();
        return null;
    }
    
    public static Object resolveConstantValue(Statement value) {
        String stringValue = resolveConstantStringValue(value);
        if (stringValue != null)
            return stringValue;
        if (value instanceof NumericLiteral)
            return Integer.valueOf(((NumericLiteral) value).getIntValue());
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public static Statement findHashItemValue(HashExpression hash, String keyName) {
        List<HashPairExpression> pairs = hash.getExpressions();
        for (HashPairExpression pair : pairs) {
            String currentKeyName = resolveConstantStringValue(pair.getKey());
            if (keyName.equals(currentKeyName))
                return pair.getValue();
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public static Statement getArgumentValue(CallExpression callExpression, int argIndex) {
        List<Statement> expressions = callExpression.getArgs().getExpressions();
        if (expressions.size() > argIndex)
            return expressions.get(0);
        return null;
    }
    
}
