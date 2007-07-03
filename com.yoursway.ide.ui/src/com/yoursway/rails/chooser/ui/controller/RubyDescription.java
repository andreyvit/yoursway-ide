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
        return rubyInstance.getVersionAsString();
    }
    
    public File getLocation() {
        return rubyInstance.getLocation();
    }
    
    public RubyInstance getRubyInstance() {
        return rubyInstance;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rubyInstance == null) ? 0 : rubyInstance.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final RubyDescription other = (RubyDescription) obj;
        if (rubyInstance == null) {
            if (other.rubyInstance != null)
                return false;
        } else if (!rubyInstance.equals(other.rubyInstance))
            return false;
        return true;
    }
    
}
