package com.yoursway.ide.projects;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import com.yoursway.ide.projects.commands.NoRailsApplicationAtGivenLocation;
import com.yoursway.ide.projects.commands.ProjectLocationPreference;
import com.yoursway.ide.projects.commands.ProjectNamingUtils;
import com.yoursway.ide.projects.commands.RailsSkeletonGenerator;
import com.yoursway.ide.projects.mru.MostRecentProjects;
import com.yoursway.ide.ui.Activator;
import com.yoursway.ide.windowing.RailsWindow;
import com.yoursway.ide.windowing.RailsWindowModel;
import com.yoursway.rails.RailsInstance;
import com.yoursway.rails.chooser.RailsProvider;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.rails.core.projects.RailsProjectsCollection;
import com.yoursway.utils.ProjectUtils;

public class YourSwayProjects {
    
    private static final class RenameSupport {
        
        private final Map<IProject, IProject> sourceToDestination = new HashMap<IProject, IProject>();
        
        private final Map<IProject, IProject> destinationToSource = new HashMap<IProject, IProject>();
        
        public synchronized void add(IProject source, IProject destination) {
            sourceToDestination.put(source, destination);
            destinationToSource.put(destination, source);
        }
        
        public synchronized void remove(IProject destination) {
            IProject source = destinationToSource.remove(destination);
            if (source != null)
                sourceToDestination.remove(source);
        }
        
        public synchronized boolean isSource(IProject project) {
            return sourceToDestination.containsKey(project);
        }
        
        public synchronized IProject getSource(IProject project) {
            return destinationToSource.get(project);
        }
        
    }
    
    private static final RenameSupport RENAME_SUPPORT = new RenameSupport();
    
    public static boolean isRenaming(IProject source) {
        return RENAME_SUPPORT.isSource(source);
    }
    
    public static void rename(final IProject source, final IProject destination, final File newLocation)
            throws ProjectRenameFailed {
        try {
            RailsProject oldRailsProject = RailsProjectsCollection.instance().get(source);
            try {
                RENAME_SUPPORT.add(source, destination);
                
                WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
                    
                    @Override
                    protected void execute(IProgressMonitor monitor) throws CoreException,
                            InvocationTargetException, InterruptedException {
                        try {
                            IProjectDescription description = source.getDescription();
                            File oldPath = description.getLocation().toFile().getCanonicalFile();
                            File location;
                            if (newLocation == null)
                                location = oldPath.getParentFile();
                            else
                                location = newLocation;
                            
                            File newPath = new File(location, destination.getName()).getCanonicalFile();
                            description.setLocation(new Path(newPath.getAbsolutePath()));
                            
                            boolean wasOpen = source.isOpen();
                            source.delete(false, true, null);
                            if (!oldPath.equals(newPath))
                                if (!oldPath.renameTo(newPath))
                                    throw new ProjectRenameFailed(NLS.bind(
                                            "Cannot rename folder {0} into {1}", oldPath, newPath));
                            destination.create(description, null);
                        } catch (ProjectRenameFailed e) {
                            throw new InvocationTargetException(e);
                        } catch (IOException e) {
                            throw new InvocationTargetException(e);
                        }
                    }
                    
                };
                operation.run(null);
                RailsProject railsProject = RailsProjectsCollection.instance().get(destination);
                Assert.isNotNull(railsProject);
                RailsWindowModel.instance().replaceProject(oldRailsProject, railsProject);
            } finally {
                RENAME_SUPPORT.remove(destination);
            }
        } catch (InvocationTargetException e) {
            final Throwable te = e.getTargetException();
            if (te instanceof ProjectRenameFailed)
                throw (ProjectRenameFailed) te;
            else
                throw new ProjectRenameFailed(te);
        } catch (InterruptedException e) {
            // WorkspaceModifyOperation.run converts OperationCanceledException into InterruptedException
            throw new OperationCanceledException(e.getMessage());
        }
    }
    
    public static void openRailsApplication(final File location) throws NoRailsApplicationAtGivenLocation {
        if (!ProjectUtils.looksLikeRailsApplication(location))
            throw new NoRailsApplicationAtGivenLocation(location);
        IProject existingProject = findProjectByLocation(location);
        if (existingProject != null)
            return;
        Job job = new Job("Opening Rails application") {
            
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                SubMonitor progress = SubMonitor.convert(monitor, 30);
                String projectName = location.getName();
                IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
                try {
                    IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(
                            project.getName());
                    description.setNatureIds(new String[] { ProjectUtils.DLTK_RUBY_NATURE,
                            ProjectUtils.YOURSWAY_RAILS_NATURE });
                    description.setLocationURI(location.toURI());
                    project.create(description, progress.newChild(20));
                    project.open(progress.newChild(10));
                    YourSwayProjects.projectUsed(project);
                    YourSwayProjects.openProjectInNewWindow(project);
                } catch (Exception e) {
                    if (project.exists())
                        try {
                            project.delete(false, true, null);
                        } catch (CoreException e1) {
                            Activator.unexpectedError(e1);
                        }
                    return new Status(IStatus.ERROR, Activator.PLUGIN_ID, NLS.bind("Cannot open project {0}",
                            location), e);
                } finally {
                    if (monitor != null)
                        monitor.done();
                }
                
                return Status.OK_STATUS;
            }
            
        };
        job.schedule();
    }
    
    public static void createRailsApplication() {
        Job job = new Job("Creating application") {
            
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                SubMonitor subMonitor = SubMonitor.convert(monitor, 70);
                String projectName = chooseProjectName();
                IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
                try {
                    
                    IProjectDescription description = createProjectDescription(project, projectName);
                    project.create(description, subMonitor.newChild(20));
                    project.open(subMonitor.newChild(10));
                    
                    RailsInstance railsInstance = RailsProvider.getInstance()
                            .getChosenRailsInstanceInterpreter();
                    if (railsInstance == null)
                        return Status.CANCEL_STATUS;
                    
                    System.out.println("Rails " + railsInstance.getVersionAsString() + " pathes:");
                    for (String path : railsInstance.getPaths())
                        System.out.println("  - " + path);
                    
                    RailsSkeletonGenerator.getInstance().putSkeletonInto(project.getLocation().toFile(),
                            railsInstance, subMonitor.newChild(20));
                    project.refreshLocal(IResource.DEPTH_INFINITE, subMonitor.newChild(20));
                    
                    YourSwayProjects.projectUsed(project);
                    YourSwayProjects.openProjectInNewWindow(project);
                } catch (Exception e) {
                    if (project.exists())
                        try {
                            project.delete(true, null);
                        } catch (CoreException e1) {
                            Activator.unexpectedError(e1);
                        }
                    return new Status(IStatus.ERROR, Activator.PLUGIN_ID, NLS.bind(
                            "Cannot create project {0}", projectName), e);
                } finally {
                    if (monitor != null)
                        monitor.done();
                }
                return Status.OK_STATUS;
            }
            
            private IProjectDescription createProjectDescription(IProject project, String projectName) {
                IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(
                        project.getName());
                description.setNatureIds(new String[] { ProjectUtils.DLTK_RUBY_NATURE,
                        ProjectUtils.YOURSWAY_RAILS_NATURE });
                File projectLocation = ProjectLocationPreference.getNewProjectLocation();
                description.setLocationURI(new File(projectLocation, projectName).toURI());
                return description;
            }
            
        };
        job.schedule();
    }
    
    protected static void projectUsed(IProject project) {
        MostRecentProjects.instance().locationUsed(project.getLocation().toFile());
    }
    
    private static IProjectDescription createProjectDescription(IProject project, String projectName) {
        IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(
                project.getName());
        description.setNatureIds(new String[] { ProjectUtils.DLTK_RUBY_NATURE,
                ProjectUtils.YOURSWAY_RAILS_NATURE });
        File projectLocation = ProjectLocationPreference.getNewProjectLocation();
        description.setLocationURI(new File(projectLocation, projectName).toURI());
        return description;
    }
    
    private static String chooseProjectName() {
        File location = ProjectLocationPreference.getNewProjectLocation();
        final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        for (String projectName : ProjectNamingUtils.getProjectNamesIterable()) {
            File projectFolder = new File(location, projectName);
            if (!projectFolder.exists() && !root.getProject(projectName).exists())
                return projectName;
        }
        throw new AssertionError("Unreachable");
    }
    
    public static void closeProject(RailsProject railsProject) {
        IProject project = railsProject.getProject();
        try {
            project.delete(false, true, null);
        } catch (CoreException e) {
            Activator.unexpectedError(e);
        }
    }
    
    public static void makeSureAllWindowsExist() throws WorkbenchException {
        for (RailsProject railsProject : RailsProjectsCollection.instance().getAll()) {
            Collection<RailsWindow> windows = RailsWindowModel.instance().findWindows(railsProject);
            if (windows.isEmpty()) {
                projectUsed(railsProject.getProject());
                openProjectInNewWindow(railsProject.getProject());
            }
        }
    }
    
    public static void openProjectInNewWindow(final IProject project) throws WorkbenchException {
        RailsProject railsProject = RailsProjectsCollection.instance().get(project);
        if (railsProject != null) {
            IWorkbenchWindow window = findUnallocatedWindow();
            if (window == null)
                window = PlatformUI.getWorkbench().openWorkbenchWindow(project);
            final IWorkbenchWindow finalWindow = window;
            RailsWindowModel.instance().getWindow(window).setRailsProject(railsProject);
            
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    for (IWorkbenchPage page : finalWindow.getPages())
                        page.close();
                    try {
                        finalWindow.openPage(project);
                    } catch (WorkbenchException e) {
                        Activator.unexpectedError(e);
                    }
                }
            });
        }
    }
    
    public static IProject findProjectByLocation(File location) {
        IContainer[] containers = ResourcesPlugin.getWorkspace().getRoot().findContainersForLocation(
                new Path(location.getPath()));
        for (IContainer container : containers)
            if (container.getType() == IResource.PROJECT)
                return (IProject) container;
        return null;
    }
    
    private static IWorkbenchWindow findUnallocatedWindow() {
        IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
        for (IWorkbenchWindow window : windows) {
            RailsProject windowRailsProject = RailsWindowModel.instance().getWindow(window).getRailsProject();
            if (windowRailsProject == null)
                return window;
        }
        return null;
    }
    
}
