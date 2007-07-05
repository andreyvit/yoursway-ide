package com.yoursway.rails.core.migrations.internal;

import org.eclipse.core.resources.IFile;

public class RailsMigrationInfo {
    
    private final IFile file;
    private final int ordinal;
    private final String expectedClassName;
    
    public RailsMigrationInfo(IFile file, int ordinal, String expectedClassName) {
        this.file = file;
        this.ordinal = ordinal;
        this.expectedClassName = expectedClassName;
    }
    
    public IFile getFile() {
        return file;
    }
    
    public int getOrdinal() {
        return ordinal;
    }
    
    public String getExpectedClassName() {
        return expectedClassName;
    }
    
}
