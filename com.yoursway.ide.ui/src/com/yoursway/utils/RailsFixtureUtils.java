package com.yoursway.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

import com.yoursway.common.resources.PathUtils;
import com.yoursway.rails.commons.RailsNamingConventions;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.rails.core.projects.RailsProjectsCollection;

public class RailsFixtureUtils {

    public static IFile getModelFileByFixture(IFile fixtureFile) {
        IProject project = fixtureFile.getProject();
        RailsProject railsProject = RailsProjectsCollection.instance().get(project);
        String[] pathComponents = PathUtils.determinePathComponents(fixtureFile.getProject().getFolder(
                RailsNamingConventions.TEST_FIXTURES), fixtureFile);
        pathComponents[pathComponents.length - 1] = railsProject.getInflector().singularize(
                pathComponents[pathComponents.length - 1]);
        IPath modelFilePath = fixtureFile.getProject().getFolder(RailsNamingConventions.APP_MODELS)
                .getFullPath();
        for (String s : pathComponents) {
            modelFilePath.append(s);
        }
        return PathUtils.getFile(project, modelFilePath);
    }
    
}
