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
    
    public static abstract class FailedToConvertProjectException extends Exception {
        
        public FailedToConvertProjectException() {
            super();
        }
        
        public FailedToConvertProjectException(Throwable cause) {
            super(cause);
        }
        
    }
    
    public static class FailedToCreateProjectBecauseAnotherProjectWithThisNameExistsException extends
            FailedToCreateProjectException {
        
        private static final long serialVersionUID = 1L;
        
    }
    
    public static class FailedToCreateProjectBecauseFolderWasNotEmptyException extends
            FailedToCreateProjectException {
        
        private static final long serialVersionUID = 1L;
        
    }
    
    public static class FailedToCreateProjectBecauseBurWasTooLazyException extends
            FailedToCreateProjectException {
        
        private static final long serialVersionUID = 1L;
        
    }
    
    public static class FailedToConvertProjectBecauseBurWasTooLazyException extends
            FailedToConvertProjectException {
        
        private static final long serialVersionUID = 1L;
        
    }
    
    /**
     * Creates a new project with a specified name in the specified parent
     * folder. The project should be put in a subfolder with a name equal to the
     * given project name. The function should only proceed if the given folder
     * either does not exist or is absolutely empty.
     * 
     * Should provide a <em>strong exception safety warranty</em>: all
     * modifications should be reverted if the function was not able to succeed
     * fully.
     * 
     * As part of it's actions, launches Rails command to generate the contents
     * of the project. Should use the most recent Rails version available in all
     * the installed Ruby interpreters.
     * 
     * Should also add the <code>lib</code> subfolders of Rails gems into the
     * buildpath of the created project as external source folders.
     * 
     * If another project with the given name already exists in the workspace,
     * <code>FailedToCreateProjectBecauseAnotherProjectWithThisNameExistsException</code>
     * should be thrown.
     * 
     * If the given folder is not empty,
     * <code>FailedToCreateProjectBecauseFolderWasNotEmptyException</code>
     * should be thrown.
     * 
     * Other errors should be reported using exceptions too.
     * 
     * @return the created and fully set up project; never <code>null</code>;
     *         never returns a partially set up project.
     * @throws FailedToCreateProjectException
     */
    public static IProject createRailsProject(File parentFolder, String projectName,
            IProgressMonitor progressMonitor) throws FailedToCreateProjectException {
        // TODO 4bur
        throw new FailedToCreateProjectBecauseBurWasTooLazyException();
    }
    
    /**
     * Converts the given Eclipse project to be usable inside YourSway IDE. As
     * part of the convertion process, correct natures should be set up and
     * Rails gems included into buildpaths.
     * 
     * This method is <em>not</em> required to provide a strong exception
     * safety warranty.
     * 
     * The method should not make any changes that would prevent using this
     * project in the Eclipse IDEs it used to be used in, unless absolutely
     * necessary. I.e., it should not remove any existing natures and builders.
     * 
     * The method is expected to execute very fast if the project does not need
     * convertion. It will be called at least once on the first activation of a
     * project after the IDE restart.
     * 
     * @param project
     *            an existing project to convert
     * @throws FailedToConvertProjectException
     */
    public static void convertToRailsProject(IProject project, IProgressMonitor progressMonitor)
            throws FailedToConvertProjectException {
        // TODO 4bur
        throw new FailedToConvertProjectBecauseBurWasTooLazyException();
    }
    
}
