package com.yoursway.rails.model.tests.projects;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

public final class FilledWorkspace extends AbstractRailsProjectsTestCase {
    
    protected IProject project;

    @Before
    public void setUp() throws CoreException {
        project = ResourcesPlugin.getWorkspace().getRoot().getProject("foo");
        create(project);
        observe();
        addSetChangeListener();
        realm.runAsyncTasks();
    }
    
    @Test
    public void notifiesWhenRemovingProject() throws Exception {
        context.checking(new Expectations() {{
            one(listener).handleSetChange(with(any(SetChangeEvent.class)));
        }});
        delete(project);
        realm.runAsyncTasks();
        realm.asyncExec(new Runnable() {

            public void run() {
                assertEquals(0, observable.size());
            }
            
        });
        realm.runAsyncTasks();
        context.assertIsSatisfied();
    }
    
}
