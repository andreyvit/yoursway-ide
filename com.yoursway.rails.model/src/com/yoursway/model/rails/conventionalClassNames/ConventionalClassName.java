package com.yoursway.model.rails.conventionalClassNames;

public abstract class ConventionalClassName implements IConventionalClassName {
    
    private final String underscoredName;
    private final QualifiedRubyClassName qualifiedRubyClassName;
    
    public ConventionalClassName(String underscoredName, QualifiedRubyClassName qualifiedRubyClassName) {
        this.underscoredName = underscoredName;
        this.qualifiedRubyClassName = qualifiedRubyClassName;
    }
    
    protected String getUnderscoredName() {
        return underscoredName;
    }
    
    protected QualifiedRubyClassName getClassName() {
        return qualifiedRubyClassName;
    }
    
}
