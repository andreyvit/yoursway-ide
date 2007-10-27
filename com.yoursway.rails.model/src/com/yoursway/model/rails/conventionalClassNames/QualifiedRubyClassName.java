package com.yoursway.model.rails.conventionalClassNames;

import com.yoursway.common.StringUtils;

/**
 * Immutable.
 * 
 * @author Andrey Tarantsov
 */
public class QualifiedRubyClassName {
    
    private final String[] components;
    
    public QualifiedRubyClassName(String doubleCommaDelimitedString) {
        components = doubleCommaDelimitedString.split("::");
    }
    
    public QualifiedRubyClassName(String[] components) {
        this.components = components;
        for (String component : components)
            if (component.contains("::"))
                throw new IllegalArgumentException(
                        "Components of QualifiedRubyClassName cannot contain double colons");
    }
    
    public String getDoubleColonDelimitedName() {
        return StringUtils.join(components, "::");
    }
    
    @Override
    public String toString() {
        return getDoubleColonDelimitedName();
    }
    
}
