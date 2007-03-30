package com.yoursway.rails;

import java.util.Set;

public class Rails {
    private final String version;
    private final Set<String> paths;
    
    public Rails(String version, Set<String> paths) {
        this.version = version;
        this.paths = paths;
    }
    
    public String getVersion() {
        return version;
    }
    
    public Set<String> getPaths() {
        return paths;
    }
}
