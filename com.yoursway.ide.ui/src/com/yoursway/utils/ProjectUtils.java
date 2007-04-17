package com.yoursway.utils;

import java.io.File;
import java.util.EnumSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

import com.yoursway.ide.rcp.YourSwayIDEApplication;

public class ProjectUtils {
    
    public enum ProjectNameStatus {
        
        VALID,

        DUPLICATE,

        INVALID
        
    }
    
    public enum KnownNature {
        
        YOURSWAY_RAILS,

        DLTK_RUBY,

        RUBYGEARS,

        RDT,

        RADRAILS,

        UNKNOWN_NATURE
        
    }
    
    public static class EclipseProjectInfo {
        
        private final String eclipseProjectName;
        
        public EclipseProjectInfo(String name, EnumSet<KnownNature> natures) {
            this.eclipseProjectName = name;
        }
        
        public String getEclipseProjectName() {
            return eclipseProjectName;
        }
        
    }
    
    /**
     * Checks if a project with the given name can be created.
     */
    public static ProjectNameStatus checkProjectName(String projectName) {
        // TODO 4bur
        return ProjectNameStatus.VALID;
    }
    
    /**
     * Determines if the given path corresponds to an Eclipse project, and what
     * kind of project that is.
     * 
     * @param path
     *            the path of the directory a project might be located at.
     * @return Information about the project at the given path, or
     *         <code>null</code> if the given directory does not contain a
     *         <code>.project</code> file.
     */
    public static EclipseProjectInfo looksLikeEclipseProject(File path) {
        // TODO 4bur
        return null;
    }
    
    /**
     * Returns if the directory looks like a Rails application. A directory is
     * considered a Rails application if it has a set of standard subdirectories (<code>app</code>,
     * <code>config</code> and <code>public</code>), and has either
     * <code>config/environment.rb</code> or <code>config/boot.rb</code>
     * files.
     * 
     * @return
     */
    public static boolean looksLikeRailsApplication(File path) {
        // TODO 4bur
        return false;
    }
    
    /**
     * Returns if any TextMate projects are located in this directory.
     * 
     * @return <code>true</code> if the directory has at least one
     *         <tt>.tmproj</tt> file.
     */
    public static boolean looksLikeTextMateProject(File path) {
        // TODO 4bur
        return false;
    }
    
    /**
     * @return <code>true</code> if an Eclipse workspace is located in the
     *         given directory.
     * @see YourSwayIDEApplication#readWorkspaceVersion
     */
    public static boolean looksLikeEclipseWorkspace(File path) {
        // TODO 4bur
        return false;
    }
    
    public static abstract class FailedToCreateProjectException extends Exception {
        
        public FailedToCreateProjectException() {
            super();
        }
        
        public FailedToCreateProjectException(Throwable cause) {
            super(cause);
        }
        
    }
    
    public static class FailedToCreateProjectBecauseBurWasTooLazyException extends
            FailedToCreateProjectException {
        
        private static final long serialVersionUID = 1L;
        
    }
    
    public static IProject createRailsProject(File parentFolder, String projectName,
            IProgressMonitor progressMonitor) throws FailedToCreateProjectException {
        // TODO 4bur
        throw new FailedToCreateProjectBecauseBurWasTooLazyException();
    }
    
}
