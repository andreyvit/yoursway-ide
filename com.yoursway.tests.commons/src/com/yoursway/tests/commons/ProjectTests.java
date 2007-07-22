package com.yoursway.tests.commons;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;

//@RunWith(JMock.class)
public class ProjectTests {
    
    protected final Mockery context = new JUnit4Mockery();
    
    protected IProject project;

    protected TestFriendlyLockBasedRealm realm;
    
    protected final StateSync<Integer> sync = new StateSync<Integer>(0);

    @Before
    public void createProjectAndRealm() throws Exception {
        project = ResourcesPlugin.getWorkspace().getRoot().getProject("test_project");
        if (project.exists())
            project.delete(true, true, null);
        project.create(null);
        project.open(null);
        
        realm = new TestFriendlyLockBasedRealm();
    }
   
    @After
    public void deleteProjectAndCleanup() throws Exception {
        project.delete(true, true, null);
    }
    
    protected void create(IFile file) throws CoreException {
        file.create(new ByteArrayInputStream("foo".getBytes()), true, null);
    }
    
    protected void create(IFolder folder) throws CoreException {
        folder.create(true, true, null);
    }
    
    protected void delete(IFile file) throws CoreException {
        file.delete(true, false, null);
    }
    
    protected void delete(IFolder folder) throws CoreException {
        folder.delete(true, false, null);
    }

}
