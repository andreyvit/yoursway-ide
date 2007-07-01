package com.yoursway.utils;

import java.io.File;

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
    
}
