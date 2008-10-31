package com.yoursway.ide.rails.projects;

import java.io.File;

import com.yoursway.ide.application.model.projects.types.ProjectType;

public class PythonProjectType extends ProjectType {
    
    @Override
    public String generateNewProjectName(File location) {
        for (String projectName : ProjectNamingUtils.getProjectNamesIterable()) {
            File projectFolder = new File(location, projectName);
            if (!projectFolder.exists())
                return projectName;
        }
        return null;
    }
    
    public String getDescriptiveName() {
        return "Python";
    }

    @Override
    public boolean recognize(File location) {
        return true;
    }
    
}
