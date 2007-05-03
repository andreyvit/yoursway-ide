package com.yoursway.utils;

import java.util.List;

import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.expressions.NumericLiteral;
import org.eclipse.dltk.ast.expressions.StringLiteral;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.ruby.ast.RubySymbolReference;

public class RubyASTUtils {
    
    public static String resolveConstantStringArgument(CallExpression call, int index) {
        return resolveConstantStringValue(getArgumentValue(call, index));
    }
    
    /**
     * @param value
     *            AST node whose value is to be resolved, can be
     *            <code>null</code>.
     * @return The resolved value, or <code>null</code> if a constant string
     *         value could not be resolved or the received argument is
     *         <code>null</code>.
     */
    public static String resolveConstantStringValue(Statement value) {
        if (value instanceof RubySymbolReference)
            return ((RubySymbolReference) value).getName();
        else if (value instanceof StringLiteral)
            return ((StringLiteral) value).getValue();
        return null;
    }
    
    public static Long resolveConstantFixnumValue(Statement value) {
        Object v = resolveConstantValue(value);
        if (v instanceof Long)
            return (Long) v;
        else if (v instanceof Number)
            return Long.valueOf(((Number) v).longValue());
        return null;
    }
    
    public static Object resolveConstantValue(Statement value) {
        String stringValue = resolveConstantStringValue(value);
        if (stringValue != null)
            return stringValue;
        if (value instanceof NumericLiteral)
            return Long.valueOf(((NumericLiteral) value).getIntValue());
        return null;
    }
    
    // FIXME: fix: broken with latest DLTK
    //    @SuppressWarnings("unchecked")
    //    public static Statement findHashItemValue(RubyHashExpression hash, String keyName) {
    //        List<RubyHashPairExpression> pairs = hash.getExpressions();
    //        for (RubyHashPairExpression pair : pairs) {
    //            String currentKeyName = resolveConstantStringValue(pair.getKey());
    //            if (keyName.equals(currentKeyName))
    //                return pair.getValue();
    //        }
    //        return null;
    //    }
    
    @SuppressWarnings("unchecked")
    public static Statement getArgumentValue(CallExpression callExpression, int argIndex) {
        List<Statement> expressions = callExpression.getArgs().getExpressions();
        if (expressions.size() > argIndex)
            return expressions.get(argIndex);
        return null;
    }
    
}
