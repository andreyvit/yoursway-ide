package com.yoursway.rails.chooser.ui.controller;

import com.yoursway.rails.RailsInstance;
import com.yoursway.rails.chooser.ui.view.IRailsDescription;
import com.yoursway.rails.chooser.ui.view.IRubyDescription;

public class RailsDescription implements IRailsDescription {
    
    private final IRubyDescription ruby;
    private final RailsInstance railsInstance;
    
    public RailsDescription(RailsInstance railsInstance) {
        this.railsInstance = railsInstance;
        this.ruby = new RubyDescription(railsInstance.getRuby());
    }
    
    public IRubyDescription getRuby() {
        return ruby;
    }
    
    public String getVersion() {
        return railsInstance.getVersionAsString();
    }
    
    public RailsInstance getRailsInstance() {
        return railsInstance;
    }
    
}
