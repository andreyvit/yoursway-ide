package com.yoursway.rails.model.oldtests.projects;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.Before;
import org.junit.Test;

import com.yoursway.rails.model.RailsProject;

public final class EmptyWorkspaceNoListener extends AbstractRailsProjectsTestCase {

    private IProject project;

    @Before
    public void setUp() {
        observe();
        project = ResourcesPlugin.getWorkspace().getRoot().getProject("foo");
    }
    
    @Test
    public void isEmptyInitially() {
        assertContents();
    }

    @Test
    public void hasOneProjectAfterCreatingFirstProject() throws Exception {
        create(project);
        realm.asyncExec(new Runnable() {

            public void run() {
                assertEquals(1, observable.size());
                RailsProject rp = (RailsProject) observable.iterator().next();
                assertEquals(project, rp.getProject());
            }
            
        });
        realm.runAsyncTasks();
    }
    
}
