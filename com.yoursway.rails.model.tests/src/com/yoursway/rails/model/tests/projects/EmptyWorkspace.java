package com.yoursway.rails.model.tests.projects;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import com.yoursway.rails.model.RailsProject;

public final class EmptyWorkspace extends AbstractRailsProjectsTestCase {

    private IProject project;

    @Before
    public void setUp() {
        observe();
        project = ResourcesPlugin.getWorkspace().getRoot().getProject("foo");
        addSetChangeListener();
    }

    @Test
    public void notifiesAboutAddingProject() throws Exception {
        context.checking(new Expectations() {{
            one(listener).handleSetChange(with(any(SetChangeEvent.class)));
        }});
        create(project);
        realm.asyncExec(new Runnable() {

            public void run() {
                assertEquals(1, observable.size());
                RailsProject rp = (RailsProject) observable.iterator().next();
                assertEquals(project, rp.getProject());
            }
            
        });
        realm.runAsyncTasks();
        context.assertIsSatisfied();
    }
    
}
