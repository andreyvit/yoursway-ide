package com.yoursway.databinding.resources.tests.shallow;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.junit.Before;
import org.junit.Test;

public final class EmptyContainerWithoutListeners extends AbstractShallowChildrenTests {
    
    private IFolder folder;
    private IFile file;
    
    @Before
    public void setUp() throws CoreException {
        folder = project.getFolder("bar");
        folder.create(true, true, null);
        file = folder.getFile("foo");
        observe(folder);
    }
    
    @Test
    public void isReportedAsEmpty() {
        assertContents();
    }
    
    @Test
    public void hasOneChildAfterAddingOneChild() throws CoreException {
        create(file);
        assertContents(file);
    }
    
    @Test
    public void reportsActualContentsEvenBeforeWorkspaceFiresEvent() throws Exception {
        WorkspaceJob job = new WorkspaceJob("Test") {
            
            @Override
            public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
                create(file);
                sync.setState(1);
                sync.waitState(2);
                return Status.OK_STATUS;
            }
            
        };
        job.schedule();
        sync.waitState(1);
        realm.runAsyncTasks();
        assertContents(file);
        
        sync.setState(2);
        job.join();
        assertContents(file);
    }
    
}
