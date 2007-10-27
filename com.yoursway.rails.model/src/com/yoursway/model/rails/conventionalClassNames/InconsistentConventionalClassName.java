package com.yoursway.model.rails.conventionalClassNames;

public class InconsistentConventionalClassName extends ConventionalClassName {
    
    public InconsistentConventionalClassName(String underscoredName, QualifiedRubyClassName qualifiedRubyClassName) {
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
        visitor.visitInconsistentName(this);
    }
    
}
