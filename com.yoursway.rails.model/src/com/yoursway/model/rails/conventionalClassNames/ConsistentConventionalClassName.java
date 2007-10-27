package com.yoursway.model.rails.conventionalClassNames;

public class ConsistentConventionalClassName extends ConventionalClassName {
    
    public ConsistentConventionalClassName(String underscoredName, QualifiedRubyClassName qualifiedRubyClassName) {
        super(underscoredName, qualifiedRubyClassName);
    }
    
    @Override
    public QualifiedRubyClassName getClassName() {
        return super.getClassName();
    }
    
    @Override
    public String getUnderscoredName() {
        return super.getUnderscoredName();
    }
    
    public void accept(IConventionalClassNameVisitor visitor) {
        visitor.visitConsistentName(this);
    }
    
}
