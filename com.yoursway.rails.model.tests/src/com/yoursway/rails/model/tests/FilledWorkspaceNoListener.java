package com.yoursway.rails.model.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Test;

import com.yoursway.rails.model.RailsProject;

public final class FilledWorkspaceNoListener extends AbstractRailsProjectsTestCase {
    
    protected IProject project;

    @Before
    public void setUp() throws CoreException {
        project = ResourcesPlugin.getWorkspace().getRoot().getProject("foo");
        create(project);
        observe();
        realm.runAsyncTasks();
    }
    
    @Test
    public void identityIsPreservedAcrossTwoConsecutiveCalls() throws Exception {
        realm.asyncExec(new Runnable() {
            
            public void run() {
                assertEquals(1, observable.size());
                RailsProject rp = (RailsProject) observable.iterator().next();
                RailsProject rp2 = (RailsProject) observable.iterator().next();
                assertSame(rp, rp2);
            }
            
        });
        realm.runAsyncTasks();
        context.assertIsSatisfied();
    }
    
    @Test
    public void identityIsPreservedWhenAddingAnotherProject() throws Exception {
        final RailsProject[] rp = new RailsProject[2];
        final IObservableValue mapping = repository.lookupProject(project);
        realm.asyncExec(new Runnable() {
            
            public void run() {
                rp[0] = (RailsProject) mapping.getValue();
            }
            
        });
        realm.runAsyncTasks();
        IProject pr2 = ResourcesPlugin.getWorkspace().getRoot().getProject("barrrrr");
        create(pr2);
        realm.asyncExec(new Runnable() {
            
            public void run() {
                rp[1] = (RailsProject) mapping.getValue();
            }
            
        });
        realm.runAsyncTasks();
        assertSame(rp[0], rp[1]);
        context.assertIsSatisfied();
    }
    
//    @Test
//    public void productionCodeShouldWork() throws Exception {
//        WritableValue value = new WritableValue(new Realm() {
//
//            @Override
//            public boolean isCurrent() {
//                return true;
//            }
//            
//            @Override
//            protected void syncExec(Runnable runnable) {
//                runnable.run();
//            }
//            
//        }, null, RailsProject.class);
//        DataBindingContext dbc = new DataBindingContext(realm);
//        final IObservableValue mapping = repository.lookupProject(project);
//        dbc.bindValue(value, mapping, null, null);
//        realm.runAsyncTasks();
//        RailsProject rp = (RailsProject) value.getValue();
//        assertNotNull(rp);
//        assertEquals(project, rp.getProject());
//    }
    
}
