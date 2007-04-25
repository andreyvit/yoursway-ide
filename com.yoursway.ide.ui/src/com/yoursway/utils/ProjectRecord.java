package com.yoursway.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;

public class ProjectRecord {
    File projectSystemFile;
    
    Object projectArchiveFile;
    
    String projectName;
    
    Object parent;
    
    int level;
    
    IProjectDescription description;
    
    IImportStructureProvider provider;
    
    /**
     * Create a record for a project based on the info in the file.
     * 
     * @param file
     */
    ProjectRecord(File file) {
        projectSystemFile = file;
        setProjectName();
    }
    
    /**
     * @param file
     *            The Object representing the .project file
     * @param parent
     *            The parent folder of the .project file
     * @param level
     *            The number of levels deep in the provider the file is
     * @param entryProvider
     *            The provider for the archive file that contains it
     */
    ProjectRecord(Object file, Object parent, int level, IImportStructureProvider entryProvider) {
        this.projectArchiveFile = file;
        this.parent = parent;
        this.level = level;
        this.provider = entryProvider;
        setProjectName();
    }
    
    /**
     * Set the name of the project based on the projectFile.
     */
    private void setProjectName() {
        IProjectDescription newDescription = null;
        try {
            if (projectArchiveFile != null) {
                InputStream stream = provider.getContents(projectArchiveFile);
                if (stream != null) {
                    newDescription = IDEWorkbenchPlugin.getPluginWorkspace().loadProjectDescription(stream);
                    stream.close();
                }
            } else {
                IPath path = new Path(projectSystemFile.getPath());
                // if the file is in the default location, use the directory
                // name as the project name
                if (isDefaultLocation(path)) {
                    projectName = path.segment(path.segmentCount() - 2);
                    newDescription = IDEWorkbenchPlugin.getPluginWorkspace().newProjectDescription(
                            projectName);
                } else {
                    newDescription = IDEWorkbenchPlugin.getPluginWorkspace().loadProjectDescription(path);
                }
            }
        } catch (CoreException e) {
            // no good couldn't get the name
        } catch (IOException e) {
            // no good couldn't get the name
        }
        
        if (newDescription == null) {
            this.description = null;
            projectName = ""; //$NON-NLS-1$
        } else {
            this.description = newDescription;
            projectName = this.description.getName();
        }
    }
    
    /**
     * Returns whether the given project description file path is in the default
     * location for a project
     * 
     * @param path
     *            The path to examine
     * @return Whether the given path is the default location for a project
     */
    private boolean isDefaultLocation(IPath path) {
        // The project description file must at least be within the project,
        // which is within the workspace location
        if (path.segmentCount() < 2)
            return false;
        return path.removeLastSegments(2).toFile().equals(Platform.getLocation().toFile());
    }
    
    /**
     * Get the name of the project
     * 
     * @return String
     */
    public String getProjectName() {
        return projectName;
    }
}
