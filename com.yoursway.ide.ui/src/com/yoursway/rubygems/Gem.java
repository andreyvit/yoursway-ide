package com.yoursway.rubygems;

public class Gem implements IGem {
    private String name;
    private String version;
    private String directory;
    private String binDir;
    private String[] requirePaths;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getDirectory() {
        return directory;
    }
    
    public void setDirectory(String directory) {
        this.directory = directory;
    }
    
    public String getBinDir() {
        return binDir;
    }
    
    public void setBinDir(String binDir) {
        this.binDir = binDir;
    }
    
    public String[] getRequirePaths() {
        return requirePaths;
    }
    
    public void setRequirePaths(String[] requirePaths) {
        this.requirePaths = requirePaths;
    }
}
