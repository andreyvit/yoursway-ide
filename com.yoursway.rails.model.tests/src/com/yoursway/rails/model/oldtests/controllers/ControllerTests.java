package com.yoursway.rails.model.oldtests.controllers;

import static com.yoursway.common.YSCollections.sortedArrayListOf;
import static info.javelot.functionalj.Functions.map;
import static org.junit.Assert.assertArrayEquals;
import info.javelot.functionalj.Function1Impl;
import info.javelot.functionalj.FunctionException;

import java.util.Comparator;
import java.util.Set;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Test;

import com.yoursway.rails.commons.RailsNamingConventions;
import com.yoursway.rails.model.RailsController;
import com.yoursway.rails.model.RailsProject;
import com.yoursway.rails.model.RailsRepository;
import com.yoursway.tests.commons.AbstractObservableSetTestCase;

public class ControllerTests extends AbstractObservableSetTestCase<RailsController> {
    
    private final class ResourceNameComparator implements Comparator<IResource> {
        public int compare(IResource o1, IResource o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
    
    private final class MapRailsControllerToFile extends Function1Impl<IFile, RailsController> {
        
        public IFile call(RailsController c) throws FunctionException {
            return c.getFile();
        }
    }
    
    protected RailsRepository repository;
    
    protected IProject project;
    
    protected IFolder app;
    
    protected IFolder appControllers;
    
    protected IFile fooController;
    
    protected IFile barController;
    
    @Before
    public void createRepository() {
        repository = new RailsRepository(realm);
        project = ResourcesPlugin.getWorkspace().getRoot().getProject("foo");
        app = project.getFolder(RailsNamingConventions.APP);
        appControllers = project.getFolder(RailsNamingConventions.APP_CONTROLLERS);
        fooController = appControllers.getFile("foo_controller.rb");
        barController = appControllers.getFile("bar_controller.rb");
    }
    
    public void observe() {
        final IObservableValue mapping = repository.lookupProject(project);
        realm.runAsyncTasks();
        realm.asyncExec(new Runnable() {
            
            public void run() {
                RailsProject railsProject = (RailsProject) mapping.getValue();
                observable = railsProject.getControllers();
            }
            
        });
        realm.runAsyncTasks();
    }
    
    @Test
    public void containsControllers() throws CoreException {
        create(project);
        create(app);
        create(appControllers);
        create(fooController);
        create(barController);
        observe();
        realm.asyncExec(new Runnable() {
            
            public void run() {
                assertArrayEquals(new IFile[] { barController, fooController }, sortedArrayListOf(
                        map(new MapRailsControllerToFile(), (Set<RailsController>) observable),
                        new ResourceNameComparator()).toArray());
            }
            
        });
        realm.runAsyncTasks();
    }
    
}
