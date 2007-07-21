package com.yoursway.databinding.resources.tests.shallow;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import com.yoursway.databinding.resources.tests.SetChangeEventMatcher;

public final class NonExistantContainer extends AbstractShallowChildrenTests {
    
    private IFolder parentFolder;
    private IFolder folder;
    private IFile file;
    
    @Before
    public void setUp() throws CoreException {
        parentFolder = project.getFolder("bar");
        folder = parentFolder.getFolder("boz");
        file = folder.getFile("foo");
        observe(folder);
        addSetChangeListener();
    }
    
    @Test
    public void isReportedAsEmpty() {
        assertContents();
    }

    @Test
    public void handlesWhenContainerAppears() throws Exception {
        context.checking(new Expectations() {
            {
                one(listener).handleSetChange(with(new SetChangeEventMatcher().additions(file)));
            }
        });
        create(parentFolder);
        WorkspaceJob job = new WorkspaceJob("Test") {
            
            @Override
            public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
                create(folder);
                create(file);
                return Status.OK_STATUS;
            }
            
        };
        job.schedule();
        job.join();
        realm.runAsyncTasks();
        context.assertIsSatisfied();
    }
    
    @Test
    public void handlesWhenGrandContainerAppears() throws Exception {
        context.checking(new Expectations() {
            {
                one(listener).handleSetChange(with(new SetChangeEventMatcher().additions(file)));
            }
        });
        WorkspaceJob job = new WorkspaceJob("Test") {
            
            @Override
            public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
                create(parentFolder);
                create(folder);
                create(file);
                return Status.OK_STATUS;
            }
            
        };
        job.schedule();
        job.join();
        realm.runAsyncTasks();
        context.assertIsSatisfied();
    }
    
}
