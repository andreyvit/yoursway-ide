package com.yoursway.model.resource.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.common.resources.ResourceSwitch;
import com.yoursway.common.resources.ResourceUtils;
import com.yoursway.model.repository.IBasicModelChangesRequestor;
import com.yoursway.model.repository.IBasicModelRegistry;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.ISnapshotBuilder;
import com.yoursway.model.repository.ModelDelta;
import com.yoursway.model.repository.SnapshotBuilder;
import com.yoursway.model.resource.IResourceModelRoot;
import com.yoursway.model.resource.IResourceProject;
import com.yoursway.model.tracking.IMapSnapshot;

public class ResourceModel {
    
    private abstract class Update {
        
        public abstract void perform(IMapSnapshot previous, ISnapshotBuilder sb);
        
    }
    
    private final class ResourceAdded extends Update {
        
        private final IResource eclipseResource;
        
        public ResourceAdded(IResource eclipseResource) {
            this.eclipseResource = eclipseResource;
        }
        
        @Override
        public void perform(final IMapSnapshot previous, final ISnapshotBuilder sb) {
            try {
                eclipseResource.accept(new ResourceSwitch() {
                    
                    @Override
                    protected boolean visitProject(IProject resource) {
                        return true;
                    }
                    
                    @Override
                    protected boolean visitFile(IFile resource) {
                        return false;
                    }
                    
                    @Override
                    protected boolean visitFolder(IFolder resource) {
                        return true;
                    }
                    
                });
            } catch (CoreException e) {
                if (ResourceUtils.isNotFound(e))
                    ; // the resource is already gone, don't care
                else
                    throw new AssertionError(e);
            }
        }
    }
    
    private final class ChangeListener extends WorkspaceResourceChangeListener {
        public void resourceChanged(IResourceChangeEvent event) {
            // TODO
        }
    }
    
    private final class InitialSnapshotRunnable implements Runnable {
        public void run() {
            createInitialSnapshot();
        }
    }
    
    private final IResourceModelRoot resourceModelRoot = new IResourceModelRoot() {
        
        public IHandle<Collection<IResourceProject>> projects() {
            return projectsCollectionHandle;
        }
        
        public IHandle<IResourceProject> project(IProject eclipseProject) {
            return new ResourceProjectHandle(eclipseProject);
        }
        
    };
    
    private final IHandle<Collection<IResourceProject>> projectsCollectionHandle = new ResourceHandle<Collection<IResourceProject>>() {
        
        @Override
        public String toString() {
            return "resource-projects";
        }
        
        //        public IModelElement getModelElement() {
        //            return resourceModelRoot;
        //        }
        
    };
    
    private WorkspaceResourceChangeListener changeListener;
    
    private final IBasicModelChangesRequestor requestor;
    
    private final IWorkspaceRoot workspaceRoot;
    
    public ResourceModel(IBasicModelRegistry registry) {
        requestor = registry.addBasicModel(IResourceModelRoot.class, resourceModelRoot);
        workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        requestor.execute(new InitialSnapshotRunnable());
    }
    
    private void createInitialSnapshot() {
        SnapshotBuilder sb = new SnapshotBuilder();
        Collection<IResourceProject> projects = new ArrayList<IResourceProject>();
        
        for (IProject eclipseProject : workspaceRoot.getProjects()) {
            projects.add(processAddedProject(sb, eclipseProject));
        }
        
        sb.put(projectsCollectionHandle, projects);
        requestor.modelChanged(sb.getSnapshot(), new ModelDelta(sb.getChangedHandles(),
                sb.getAddedElements(), sb.getRemovedElements()));
        
        // can subscribe to changes now
        changeListener = new ChangeListener();
    }
    
    private IResourceProject processAddedProject(SnapshotBuilder sb, IProject eclipseProject) {
        IResourceProject proj = new ResourceProject();
        sb.added(proj);
        sb.put(proj.name(), eclipseProject.getName());
        sb.put(proj.fileSystemLocation(), eclipseProject.getLocation().toFile());
        sb.put(proj.eclipseProject(), eclipseProject);
        sb.put(new ResourceProjectHandle(eclipseProject), proj);
        return proj;
    }
    
}
