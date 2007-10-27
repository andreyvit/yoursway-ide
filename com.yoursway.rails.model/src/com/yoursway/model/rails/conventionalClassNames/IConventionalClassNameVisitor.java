package com.yoursway.model.rails.conventionalClassNames;

public interface IConventionalClassNameVisitor {
    
    void visitConsistentName(ConsistentConventionalClassName name);
    
    void visitInconsistentName(InconsistentConventionalClassName name);
    
}
