package com.yoursway.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.yoursway.ide.rcp.YourSwayIDEApplication;
import com.yoursway.rails.models.project.RailsProject;
import com.yoursway.rails.models.project.RailsProjectsModel;
import com.yoursway.rails.windowmodel.RailsWindowModel;

public class ProjectUtils {
    private static final String[] RAILS_OBLIGATORY_DIRS = new String[] { "app", "config", "public" };
    
    public enum ProjectNameStatus {
        VALID, DUPLICATE, INVALID
    }
    
    public enum KnownNature {
        YOURSWAY_RAILS, DLTK_RUBY, RDT, RADRAILS, UNKNOWN_NATURE
    }
    
    public static String YOURSWAY_RAILS_NATURE = "com.yoursway.ide.rails.nature";
    public static String DLTK_RUBY_NATURE = "org.eclipse.dltk.ruby.core.nature";
    
    //public static String RUBYGEARS_NATURE = "com.xored.rubygears.project.core.FreezedRailsNature";
    
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
     * 
     * @throws FailedToCreateProjectException
     */
    public static ProjectNameStatus checkProjectName(String projectName)
            throws FailedToCreateProjectException {
        final IWorkspace workspace = DLTKUIPlugin.getWorkspace();
        final IStatus nameStatus = workspace.validateName(projectName, IResource.PROJECT);
        if (!nameStatus.isOK()) {
            throw new FailedToCreateProjectBecauseAnotherProjectWithThisNameExistsException(nameStatus
                    .getMessage());
        }
        return ProjectNameStatus.VALID;
    }
    
    public static IProject getProjectHandle(File fNameGroup) {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(fNameGroup.getName());
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
        final IWorkspace workspace = DLTKUIPlugin.getWorkspace();
        final String name = path.getName();
        // check whether the project name field is empty
        if (name.length() == 0) {
            return null;
        }
        // check whether the project name is valid
        final IStatus nameStatus = workspace.validateName(name, IResource.PROJECT);
        if (!nameStatus.isOK()) {
            return null;
        }
        // check whether project already exists
        final IProject handle = getProjectHandle(path);
        if (handle.exists()) {
            return null;
        }
        final String location = path.getAbsolutePath();
        // check whether location is empty
        if (location.length() == 0) {
            return null;
        }
        // check whether the location is a syntactically correct path
        if (!Path.EMPTY.isValidPath(location)) {
            return null;
        }
        // check whether the location has the workspace as prefix
        IPath projectPath = Path.fromOSString(location);
        if (Platform.getLocation().isPrefixOf(projectPath)) {
            return null;
        }
        final IStatus locationStatus = workspace.validateProjectLocation(handle, projectPath);
        if (!locationStatus.isOK()) {
            return null;
        }
        
        File projectFile = getFile(path, IProjectDescription.DESCRIPTION_FILE_NAME);
        ProjectRecord projectRecord = new ProjectRecord(projectFile);
        String[] natureIds = projectRecord.description.getNatureIds();
        EnumSet<KnownNature> natures = EnumSet.noneOf(KnownNature.class);
        for (String nature : natureIds) {
            if (nature.equalsIgnoreCase(DLTK_RUBY_NATURE)) {
                natures.add(KnownNature.DLTK_RUBY);
            }
            if (nature.equalsIgnoreCase(YOURSWAY_RAILS_NATURE)) {
                natures.add(KnownNature.YOURSWAY_RAILS);
            }
            //FIXME add other natures
        }
        return new EclipseProjectInfo(projectRecord.getProjectName(), natures);
    }
    
    public static File getFile(File directory, String name) {
        File[] contents = directory.listFiles();
        // first look for project description files
        for (int i = 0; i < contents.length; i++) {
            File file = contents[i];
            if (file.isFile() && file.getName().equals(name)) {
                return file;
            }
        }
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
        if (!hasDirectories(path, RAILS_OBLIGATORY_DIRS)) {
            return false;
        }
        File config = new File(path.getAbsolutePath() + File.separator + "config");
        if (getFile(config, "environment.rb") != null || getFile(config, "boot.rb") != null) {
            return true;
        }
        return false;
    }
    
    /**
     * Tests if path has subdirectories with given names
     * 
     * @param path
     * @param directoryList
     * @return True or False
     */
    private static boolean hasDirectories(File path, String[] directoryList) {
        File[] contents = path.listFiles();
        // first look for project description files
        HashSet<String> dirHash = new HashSet<String>();
        for (String dir : directoryList) {
            dirHash.add(dir);
        }
        
        for (int i = 0; i < contents.length; i++) {
            File file = contents[i];
            if (file.isDirectory()) {
                dirHash.remove(file.getName());
            }
        }
        return dirHash.isEmpty();
    }
    
    /**
     * Returns if any TextMate projects are located in this directory.
     * 
     * @return <code>true</code> if the directory has at least one
     *         <tt>.tmproj</tt> file.
     */
    public static boolean looksLikeTextMateProject(File path) {
        return getFile(path, ".tmproj") != null;
    }
    
    /**
     * @return <code>true</code> if an Eclipse workspace is located in the
     *         given directory.
     * @see YourSwayIDEApplication#readWorkspaceVersion
     */
    public static boolean looksLikeEclipseWorkspace(File path) {
        if (!hasDirectories(path, new String[] { ProjectImportUtils.METADATA_FOLDER }))
            return false;
        String strPath = path.getAbsolutePath().replace(File.separatorChar, '/');
        URL url;
        try {
            url = new URL("file", null, strPath);
        } catch (MalformedURLException e) {
            return false;
        } //$NON-NLS-1$
        return (YourSwayIDEApplication.readWorkspaceVersion(url) != null);
    }
    
    public static abstract class FailedToCreateProjectException extends Exception {
        
        public FailedToCreateProjectException() {
            super();
        }
        
        public FailedToCreateProjectException(Throwable cause) {
            super(cause);
        }
        
        public FailedToCreateProjectException(String message) {
            super(message);
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
        
        public FailedToCreateProjectBecauseAnotherProjectWithThisNameExistsException(String message) {
            super(message);
        }
        
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
    
    public static void openProjectInNewWindow(IProject project) throws WorkbenchException {
        RailsProject railsProject = RailsProjectsModel.getInstance().get(project);
        if (railsProject != null) {
            IWorkbenchWindow window = PlatformUI.getWorkbench().openWorkbenchWindow(project);
            RailsWindowModel.instance().getWindow(window).setRailsProject(railsProject);
        }
    }
    
}
