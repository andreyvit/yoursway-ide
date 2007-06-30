package com.yoursway.rails.chooser.ui.controller;

import com.yoursway.rails.Rails;
import com.yoursway.rails.chooser.ui.view.IRailsDescription;
import com.yoursway.rails.chooser.ui.view.IRubyDescription;

public class RailsDescription implements IRailsDescription {
    
    private final IRubyDescription ruby;
    private final Rails rails;
    
    public RailsDescription(Rails rails) {
        this.rails = rails;
        this.ruby = new RubyDescription(rails.getRuby());
    }
    
    public IRubyDescription getRuby() {
        return ruby;
    }
    
    public String getVersion() {
        return rails.getVersionAsString();
    }
    
    public Rails getRails() {
        return rails;
    }
    
}
