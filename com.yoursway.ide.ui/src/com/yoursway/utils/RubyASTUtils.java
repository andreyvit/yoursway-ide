package com.yoursway.utils;

import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.expressions.NumericLiteral;
import org.eclipse.dltk.ast.expressions.StringLiteral;
import org.eclipse.dltk.ruby.ast.RubyHashExpression;
import org.eclipse.dltk.ruby.ast.RubyHashPairExpression;
import org.eclipse.dltk.ruby.ast.RubySymbolReference;

public class RubyASTUtils {
    
    public static String resolveConstantStringArgument(CallExpression call, int index) {
        return resolveConstantStringValue(getArgumentValue(call, index));
    }
    
    /**
     * @param argNode
     *            AST node whose value is to be resolved, can be
     *            <code>null</code>.
     * @return The resolved value, or <code>null</code> if a constant string
     *         value could not be resolved or the received argument is
     *         <code>null</code>.
     */
    public static String resolveConstantStringValue(ASTNode argNode) {
        if (argNode instanceof RubySymbolReference)
            return ((RubySymbolReference) argNode).getName();
        else if (argNode instanceof StringLiteral)
            return ((StringLiteral) argNode).getValue();
        return null;
    }
    
    public static Long resolveConstantFixnumValue(ASTNode value) {
        Object v = resolveConstantValue(value);
        if (v instanceof Long)
            return (Long) v;
        else if (v instanceof Number)
            return Long.valueOf(((Number) v).longValue());
        return null;
    }
    
    public static Object resolveConstantValue(ASTNode value) {
        String stringValue = resolveConstantStringValue(value);
        if (stringValue != null)
            return stringValue;
        if (value instanceof NumericLiteral)
            return Long.valueOf(((NumericLiteral) value).getIntValue());
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public static ASTNode findHashItemValue(RubyHashExpression hash, String keyName) {
        List<RubyHashPairExpression> pairs = hash.getChilds();
        for (RubyHashPairExpression pair : pairs) {
            String currentKeyName = resolveConstantStringValue(pair.getKey());
            if (keyName.equals(currentKeyName))
                return pair.getValue();
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public static ASTNode getArgumentValue(CallExpression callExpression, int argIndex) {
        List<ASTNode> expressions = callExpression.getArgs().getChilds();
        if (expressions.size() > argIndex)
            return expressions.get(argIndex);
        return null;
    }
    
}
