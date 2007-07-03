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
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((railsInstance == null) ? 0 : railsInstance.hashCode());
        result = prime * result + ((ruby == null) ? 0 : ruby.hashCode());
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
        final RailsDescription other = (RailsDescription) obj;
        if (railsInstance == null) {
            if (other.railsInstance != null)
                return false;
        } else if (!railsInstance.equals(other.railsInstance))
            return false;
        if (ruby == null) {
            if (other.ruby != null)
                return false;
        } else if (!ruby.equals(other.ruby))
            return false;
        return true;
    }
    
}
