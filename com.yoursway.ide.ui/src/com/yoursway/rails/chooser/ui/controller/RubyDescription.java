package com.yoursway.rails.chooser.ui.controller;

import java.io.File;

import com.yoursway.rails.chooser.ui.view.IRubyDescription;
import com.yoursway.ruby.RubyInstance;

public class RubyDescription implements IRubyDescription {
    
    private final RubyInstance rubyInstance;
    
    public RubyDescription(RubyInstance rubyInstance) {
        this.rubyInstance = rubyInstance;
    }
    
    public String getVersion() {
        return rubyInstance.getVersion();
    }
    
    public File getLocation() {
        return rubyInstance.getLocation();
    }
    
    public RubyInstance getRubyInstance() {
        return rubyInstance;
    }
    
}
