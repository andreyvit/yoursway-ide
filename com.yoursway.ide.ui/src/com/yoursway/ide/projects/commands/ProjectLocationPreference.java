package com.yoursway.ide.projects.commands;

import java.io.File;

import com.yoursway.utils.SystemUtilities;

public class ProjectLocationPreference {
    
    public static File getNewProjectLocation() {
        return SystemUtilities.getInstance().getDocumentsStorageLocation();
    }
    
}
