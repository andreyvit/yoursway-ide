package com.yoursway.rails.chooser.ui.view;

public interface IRailsDescription {
    
    IRubyDescription getRuby();
    
    String getVersion();
    
    public boolean equals(Object obj);
    
    public int hashCode();
    
}