package com.yoursway.ide.projects.commands;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.RailsInstance;
import com.yoursway.rails.chooser.RailsProvider;
import com.yoursway.rails.search.RailsSearching;
import com.yoursway.utils.ProjectUtils;

public class CreateRailsApplicationHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Job job = new Job("Creating application") {
            
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                SubMonitor subMonitor = SubMonitor.convert(monitor, 50);
                String projectName = chooseProjectName();
                IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
                try {
                    
                    IProjectDescription description = createProjectDescription(project, projectName);
                    project.create(description, subMonitor.newChild(20));
                    project.open(subMonitor.newChild(10));
                    
                    RailsInstance railsInstance = RailsProvider.getInstance().getChosenRailsInstanceInterpreter();
                    if (railsInstance == null)
                        return Status.CANCEL_STATUS;
                    
                    System.out.println("Rails " + railsInstance.getVersionAsString() + " pathes:");
                    for (String path : railsInstance.getPaths())
                        System.out.println("  - " + path);
                    
                    RailsSkeletonGenerator.getInstance().putSkeletonInto(project.getLocation().toFile(),
                            railsInstance, subMonitor.newChild(20));
                    
                    ProjectUtils.openProjectInNewWindow(project);
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
            
            @Override
            public boolean belongsTo(Object family) {
                if (family == RailsSearching.NEEDS_RAILS_FAMILY)
                    return true;
                return super.belongsTo(family);
            }
            
        };
        job.schedule();
        return null;
    }
    
    private String chooseProjectName() {
        File location = ProjectLocationPreference.getNewProjectLocation();
        final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        for (String projectName : ProjectNamingUtils.getProjectNamesIterable()) {
            File projectFolder = new File(location, projectName);
            if (!projectFolder.exists() && !root.getProject(projectName).exists())
                return projectName;
        }
        throw new AssertionError("Unreachable");
    }
    
}
