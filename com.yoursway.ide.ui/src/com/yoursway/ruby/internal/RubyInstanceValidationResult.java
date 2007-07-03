package com.yoursway.ruby.internal;

import org.eclipse.core.runtime.Assert;

import com.yoursway.rails.Version;

public class RubyInstanceValidationResult {
    
    private final boolean isValid;
    private final Version version;
    
    public RubyInstanceValidationResult(boolean isValid, Version version) {
        Assert.isTrue(isValid == (version != null));
        this.isValid = isValid;
        this.version = version;
    }
    
    public boolean isValid() {
        return isValid;
    }
    
    public Version getVersion() {
        if (!isValid)
            throw new IllegalStateException("Cannot get version of invalid Ruby interpreter");
        return version;
    }
    
}
