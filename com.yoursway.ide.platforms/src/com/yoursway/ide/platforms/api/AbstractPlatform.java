package com.yoursway.ide.platforms.api;

public abstract class AbstractPlatform implements PlatformSupport {
    
    public GlobalMenuSupport globalMenuSupport() {
        return null;
    }
    
    public NativeGlobalAlerts nativeGlobalAlerts() {
        return null;
    }
    
}
