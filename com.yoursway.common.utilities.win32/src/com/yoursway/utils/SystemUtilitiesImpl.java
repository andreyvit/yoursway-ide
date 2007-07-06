package com.yoursway.utils;

import java.io.File;
import java.net.URL;

import com.yoursway.common.rcp.name.MultiChoiceName;

public class SystemUtilitiesImpl extends SystemUtilities {
    
    static {
        System.loadLibrary("sysutils");
    }
    
    @Override
    public File getDocumentsStorageLocation() {
        return new File(getUserHomeDir());
    }
    
    @Override
    public File getRCPWorkspaceStorageLocation(MultiChoiceName rcpName) {
        return new File(getApplicationDataLocation0(), rcpName.getLongNameWithSpaces());
    }
    
    private String getUserHomeDir() {
        return getMyDocumentsLocation0();
    }
    
    @Override
    public File getProgramFilesLocation() {
        return new File(getProgramFilesLocation0());
    }
    
    @Override
    protected File doGetFileSystemPathFromLocalURL(URL url) {
        return new File(url.getPath()).getAbsoluteFile();
    }
    
    @Override
    public boolean isOkayToTreatAsRuby(File file) {
        return isConsoleApplication(file.getPath());
    }
    
    private native boolean isConsoleApplication(String filename);
    
    private native String getMyDocumentsLocation0();
    
    private native String getProgramFilesLocation0();
    
    private native String getApplicationDataLocation0();
    
}
