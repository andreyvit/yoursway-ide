package com.yoursway.rails.chooser.ui.controller;

import java.io.File;

import com.yoursway.rails.chooser.ui.view.IRubyDescription;
import com.yoursway.ruby.RubyInstallation;

public class RubyDescription implements IRubyDescription {
    
    private final RubyInstallation rubyInstallation;
    
    public RubyDescription(RubyInstallation rubyInstallation) {
        this.rubyInstallation = rubyInstallation;
    }
    
    public String getVersion() {
        return rubyInstallation.getVersion();
    }
    
    public File getLocation() {
        return rubyInstallation.getLocation();
    }
    
    public RubyInstallation getRubyInstallation() {
        return rubyInstallation;
    }
    
}
