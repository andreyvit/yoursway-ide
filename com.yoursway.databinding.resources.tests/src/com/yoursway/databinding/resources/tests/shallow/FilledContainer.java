package com.yoursway.databinding.resources.tests.shallow;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import com.yoursway.databinding.resources.tests.SetChangeEventMatcher;

public final class FilledContainer extends AbstractShallowChildrenTests {
    
    private IFolder folder;
    private IFile file;
    private IFolder parentFolder;
    
    @Before
    public void setUp() throws CoreException {
        parentFolder = project.getFolder("bar");
        folder = parentFolder.getFolder("boz");
        file = folder.getFile("foo");
        
        create(parentFolder);
        create(folder);
        create(file);
        
        observe(folder);
        addSetChangeListener();
    }
    
    @Test
    public void reallyContainsWhatItShould() throws CoreException {
        assertContents(file);
    }
    
    @Test
    public void notifiesWhenRemovingOneChild() throws CoreException {
        expectFileRemoved();
        file.delete(true, null);
        realm.runAsyncTasks();
        assertContents();
        context.assertIsSatisfied();
    }
    
    @Test
    public void handlesWhenContainerDisappears() throws Exception {
        expectFileRemoved();
        delete(folder);
        realm.runAsyncTasks();
        assertContents();
        context.assertIsSatisfied();
    }
    
    @Test
    public void handlesWhenGrandContainerDisappears() throws Exception {
        expectFileRemoved();
        delete(parentFolder);
        realm.runAsyncTasks();
        assertContents();
        context.assertIsSatisfied();
    }
    
    @Test
    public void continuesToReportContentsEvenAfterBackingStorageIsDeleted() throws Exception {
        file.getLocation().toFile().delete();
        folder.getLocation().toFile().delete();
        realm.runAsyncTasks();
        assertContents(file);
        context.assertIsSatisfied();
    }

    private void expectFileRemoved() {
        context.checking(new Expectations() {
            {
                one(listener).handleSetChange(with(new SetChangeEventMatcher().removals(file)));
            }
        });
    }
    
}
