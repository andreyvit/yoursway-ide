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

public final class EmptyContainer extends AbstractShallowChildrenTests {
    
    private IFolder folder;
    private IFile file;
    
    @Before
    public void setUp() throws CoreException {
        folder = project.getFolder("bar");
        create(folder);
        file = folder.getFile("foo");
        observe(folder);
        addSetChangeListener();
    }
    
    @Test
    public void notifiesWhenAddingOneChild() throws CoreException {
        expectFileAdded();
        create(file);
        realm.runAsyncTasks();
        assertContents(file);
        context.assertIsSatisfied();
    }
    
    @Test
    public void doesNotNotifyAboutChildAdditionUntilWorkspaceFiresEvent() throws Exception {
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
        forceRead();
        realm.runAsyncTasks();
        
        expectFileAdded();
        sync.setState(2);
        job.join();
        realm.runAsyncTasks();
        context.assertIsSatisfied();
    }
    
    private void expectFileAdded() {
        context.checking(new Expectations() {
            {
                one(listener).handleSetChange(with(new SetChangeEventMatcher().additions(file)));
            }
        });
    }
     
}
