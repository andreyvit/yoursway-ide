package com.yoursway.rails;

import java.util.Set;

import org.eclipse.dltk.launching.IInterpreterInstall;

public class Rails {
    private final String version;
    private final IInterpreterInstall ruby;
    private final Set<String> paths;
    
    public Rails(IInterpreterInstall ruby, String version, Set<String> paths) {
        this.version = version;
        this.ruby = ruby;
        this.paths = paths;
    }
    
    public String getVersion() {
        return version;
    }
    
    public Set<String> getPaths() {
        return paths;
    }
    
    public IInterpreterInstall getRuby() {
        return ruby;
    }
}
