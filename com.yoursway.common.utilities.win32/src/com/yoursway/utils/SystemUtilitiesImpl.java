package com.yoursway.utils;

import java.io.File;
import java.net.URL;

import com.yoursway.common.rcp.name.MultiChoiceName;

public class SystemUtilitiesImpl extends SystemUtilities {

    public File getDocumentsStorageLocation() {
        return new File(getUserHomeDir());
    }

    public File getRCPWorkspaceStorageLocation(MultiChoiceName rcpName) {
        return new File(getUserHomeDir(), "." + rcpName.getShortLowercaseName());
    }

    private String getUserHomeDir() {
        return System.getProperty("user.home");
    }
    
    protected File doGetFileSystemPathFromLocalURL(URL url) {
        return new File(url.getPath()).getAbsoluteFile();
    }

}
