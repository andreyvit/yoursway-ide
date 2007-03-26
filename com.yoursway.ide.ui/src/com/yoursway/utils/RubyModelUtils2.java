package com.yoursway.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IType;

public class RubyModelUtils2 {
    
    /**
     * @param type
     * @return The fully-qualified name of the given type, or <code>null</code>
     *         if the type cannot be represented with a fully-qualified name.
     */
    public static String[] getFullyQualifiedName(IType type) {
        Collection<String> result = new ArrayList<String>();
        if (!addFullyQualifiedNameComponents(type, result))
            return null;
        return result.toArray(new String[result.size()]);
    }
    
    public static boolean addFullyQualifiedNameComponents(IType type, Collection<String> components) {
        String typeName = type.getElementName();
        if (typeName == null || typeName.length() == 0)
            return false;
        
        IType enclosingType = null;
        IModelElement parent = type.getParent();
        if (parent != null)
            enclosingType = (IType) parent.getAncestor(IModelElement.TYPE);
        if (enclosingType != null)
            addFullyQualifiedNameComponents(enclosingType, components);
        components.add(typeName);
        return true;
    }
    
}
