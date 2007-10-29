package com.yoursway.ide.ui.railsview.shit.rails;

import com.yoursway.model.rails.conventionalClassNames.ConsistentConventionalClassName;
import com.yoursway.model.rails.conventionalClassNames.IConventionalClassName;
import com.yoursway.model.rails.conventionalClassNames.IConventionalClassNameVisitor;
import com.yoursway.model.rails.conventionalClassNames.InconsistentConventionalClassName;

public class ClassNameUtil {
    
    public static String buildName(IConventionalClassName name) {
        final String[] result = new String[1];
        name.accept(new IConventionalClassNameVisitor() {
            
            public void visitConsistentName(ConsistentConventionalClassName name) {
                result[0] = name.getUnderscoredName();
            }
            
            public void visitInconsistentName(InconsistentConventionalClassName name) {
                result[0] = name.getUnderscoredName() + "("
                        + name.getClassName().getDoubleColonDelimitedName() + ")";
            }
            
        });
        return result[0];
    }
    
}
